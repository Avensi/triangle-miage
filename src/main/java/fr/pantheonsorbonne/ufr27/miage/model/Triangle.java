package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import  java.lang.Math;

@XmlRootElement(name = "triangle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Triangle implements Serializable {
    @JacksonXmlElementWrapper(useWrapping = false)

    Point[] point;


    public Triangle(){
        this.point = new Point[3];
    }
    public Triangle(Point[] points) {
        this.point = points;
    }

    public Point[] getPoint() {
        return point;
    }

    public void setPoint(Point[] point) {
        this.point = point;
    }

    public Point[] parsePoints(List<List<String>> points) {
        Point[] finalPoints = new Point[3];
        int i;
        for(i=0; i<finalPoints.length; i++){
            finalPoints[i] = new Point(points.get(i));
        }
        return finalPoints;
    }

    public double getLength(Point p1, Point p2){
        double x = Math.abs(p2.getX() - p1.getX());
        double y = Math.abs(p2.getY() - p1.getY());
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }
    public boolean isEquilateral(){
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat dec = new DecimalFormat("#0.00000000000", decimalFormatSymbols);
        double AB = Double.parseDouble(dec.format(getLength(this.point[0], this.point[1])));
        double AC = Double.parseDouble(dec.format(getLength(this.point[0], this.point[2])));
        double BC = Double.parseDouble(dec.format(getLength(this.point[1], this.point[2])));

        return (AB == AC && AB == BC);
    }

    public double getPerimeter(){
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat dec = new DecimalFormat("#0.00000000000", decimalFormatSymbols);
        double AB = Double.parseDouble(dec.format(getLength(this.point[0], this.point[1])));
        double AC = Double.parseDouble(dec.format(getLength(this.point[0], this.point[2])));
        double BC = Double.parseDouble(dec.format(getLength(this.point[1], this.point[2])));

        return Double.parseDouble(dec.format(AB + AC + BC));
    }


    @Override
    public String toString() {
        return "Triangle{" +
                "points=" + Arrays.toString(point) +
                '}';
    }
}
