package com.kuzznya.lab.controller

import com.kuzznya.lab.model.MagneticField
import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Button
import javafx.stage.FileChooser
import java.io.File
import kotlin.math.ceil

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

        val seriesBz: Series<Number, Number> = Series()
        seriesBz.data = FXCollections.observableList(
            field.values
                .toList()
                .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
        )
        seriesBz.name = "B(z)"
        chartBZ.data.add(seriesBz)

        val seriesGradBz: Series<Number, Number> = Series()
        seriesGradBz.data = FXCollections.observableList(
            field.gradBz
                .toList()
                .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
        )
        seriesGradBz.name = "grad Bz(z)"
        chartGradBz.data.add(seriesGradBz)

        val seriesGradBxy: Series<Number, Number> = Series()
        seriesGradBxy.data = FXCollections.observableList(
            field.gradBxy
                .toList()
                .map { XYChart.Data<Number, Number>(it.first.z, it.second) }
        )
        seriesGradBxy.name = "grad Bxy(z)"
        chartGradBxy.data.add(seriesGradBxy)

        loadDataButton.isDisable = false
        exportImageButton.isDisable = false
    }

    @FXML
    fun exportImage() {

    }

}