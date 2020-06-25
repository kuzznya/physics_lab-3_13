package com.kuzznya.lab.controller

import com.kuzznya.lab.model.MagneticField
import com.kuzznya.lab.model.TheoreticalMagneticField
import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import javafx.collections.FXCollections
import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.SnapshotParameters
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.*
import javafx.scene.image.WritableImage
import javafx.stage.FileChooser
import java.io.File
import javax.imageio.ImageIO

class MainController {

    @FXML
    private lateinit var chartBZ: ScatterChart<Number, Number>
    @FXML
    private lateinit var chartGradBz: ScatterChart<Number, Number>
    @FXML
    private lateinit var chartGradBxy: ScatterChart<Number, Number>
    @FXML
    private lateinit var loadDataButton: Button
    @FXML
    private lateinit var uniformityDZButton: Button
    @FXML
    private lateinit var exportImageButton: Button
    @FXML
    private lateinit var chartPane: TabPane
    @FXML
    private lateinit var inputA: TextField
    @FXML
    private lateinit var inputI: TextField
    @FXML
    private lateinit var inputR: TextField

    private lateinit var field: MagneticField

    private var a: Double = 0.0
    private var I: Double = 0.0
    private var R: Double = 0.0

    private fun commitOrRevert(input: TextField, oldValue: Double) =
        kotlin.runCatching {
            input.text = input.text.toDouble().toString()
            return@runCatching input.text.toDouble()
        } .getOrElse {
            input.text = oldValue.toString()
            return@getOrElse oldValue
        }

    @FXML
    private fun initialize() {
        inputA.setOnAction { event: ActionEvent -> a = commitOrRevert(event.source as TextField, a) }
        inputI.setOnAction { event: ActionEvent -> I = commitOrRevert(event.source as TextField, I) }
        inputR.setOnAction { event: ActionEvent -> R = commitOrRevert(event.source as TextField, R) }
        updateTheorParams()
    }

    private fun updateTheorParams() {
        a = commitOrRevert(inputA, a)
        I = commitOrRevert(inputI, I)
        R = commitOrRevert(inputR, R)
    }

    @FXML
    fun loadData() {
        val file: File = FileChooser().showOpenDialog(Router.primaryStage.owner) ?: return
        Router.primaryStage.title = file.name

        loadDataButton.isDisable = true
        uniformityDZButton.isDisable = true
        exportImageButton.isDisable = true

        chartBZ.data.clear()
        chartGradBz.data.clear()
        chartGradBxy.data.clear()

        field = MagneticField(DataReader(file.absolutePath).loadData())

        chartBZ.data.add(
            Series("B(z)",
                FXCollections.observableList(
                    field.values
                        .toList()
                        .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
                )
            )
        )

        chartGradBz.data.add(
            Series("grad Bz(z)",
                FXCollections.observableList(
                    field.gradBz
                        .toList()
                        .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
                )
            )
        )

        chartGradBxy.data.add(
            Series("grad Bxy(z)",
                FXCollections.observableList(
                    field.gradBxy
                        .toList()
                        .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
                )
            )
        )

        loadDataButton.isDisable = false
        uniformityDZButton.isDisable = false
        exportImageButton.isDisable = false
    }

    @FXML
    fun plotTheorChart() {
        if (!this::field.isInitialized) {
            Alert(Alert.AlertType.ERROR, "No experimental points", ButtonType.OK).showAndWait()
            return
        }

        updateTheorParams()

        val theorField = TheoreticalMagneticField(a, I, R)

        chartBZ.data.add(
            Series("B(z) theor.",
                FXCollections.observableList(
                    theorField.getBValues(field.data.keys)
                        .toList()
                        .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
                )
            )
        )
    }

    @FXML
    fun showUniformityDeltaZ() {
        Alert(Alert.AlertType.INFORMATION,
            List(10) {index ->
                "${index + 1}% \t Δz = ${field.deltaZUniform((index + 1) / 100.0)}" }
                .joinToString(separator = "\n"),
            ButtonType.CLOSE).show()
    }

    @FXML
    fun exportImage() {
        val chooser = FileChooser()
        chooser.extensionFilters.add(FileChooser.ExtensionFilter(
            "image files (*.png)", "*.png"
        ))

        var file = chooser.showSaveDialog(Router.primaryStage) ?: return

        if (!file.name.toUpperCase().endsWith(".PNG"))
            file = File(file.absolutePath + ".png")

        val params = SnapshotParameters()
        val image: WritableImage = when {
            chartPane.tabs[0].isSelected -> chartBZ.snapshot(params, null)
            chartPane.tabs[1].isSelected -> chartGradBz.snapshot(params, null)
            else -> chartGradBxy.snapshot(params, null)
        }

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file)
    }

}