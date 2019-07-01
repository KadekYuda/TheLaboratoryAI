/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

/**
 *
 * @author yudai
 */
public class Move {
  private String worker;
  private String place;
  private String options;
  private Double value;

  public Move(String worker, String place, String options, Double value) {
    this.worker =  worker;
    this.place = place;
    this.options = options;
    this.value = value;
  }

  public Move(String worker, String place, String options) {
    this.worker =  worker;
    this.place = place;
    this.options = options;
    this.value = new Double(0);
  }

  public Move(Double value) {
    this.value = value;
    this.worker = "";
    this.place = "";
    this.options = "";
  }

  public String getWorker() {
    return worker;
  }

  public String getPlace() {
    return place;
  }

  public String getOptions() {
    return options;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public String toString() {
    return worker + " " + place +" " + options;
  }

}
