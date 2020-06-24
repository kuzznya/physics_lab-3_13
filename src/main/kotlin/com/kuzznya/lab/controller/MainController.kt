package com.kuzznya.lab.controller

import com.kuzznya.lab.model.MagneticField
import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import javafx.collections.FXCollections
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.SnapshotParameters
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Button
import javafx.scene.control.TabPane
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
    private lateinit var exportImageButton: Button

    private lateinit var field: MagneticField

    @FXML
    private fun initialize() {}

    @FXML
    fun loadData() {
        val file: File = FileChooser().showOpenDialog(Router.primaryStage.owner) ?: return
        Router.primaryStage.title = file.name

        loadDataButton.isDisable = true
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
        exportImageButton.isDisable = false
    }

    @FXML
    fun exportImage() {

    }

}