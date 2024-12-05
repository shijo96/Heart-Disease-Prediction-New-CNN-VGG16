$(function () {
    "use strict";


 $('.scroll-list').slimScroll({  
    height: '406px' 
  });

        var options = {
          series: [440, 550],
          chart: {
        type: 'donut',
        width: 200,
      },
      dataLabels: {
      enabled: false,
      },
    colors: ['#0d6efd', '#cfe2ff'],
    legend: {
      show: false,
    },
      
      plotOptions: {
        pie: {
          donut: {
          size: '75%',
            labels: {
            show: true,
            total: {
              showAlways: true,
              show: true
            }
            },
          }
        }
      },
    labels: ["Woman", "Man"],
        responsive: [{
          breakpoint: 1600,
          options: {
            chart: {
        width: 175,
            }
          }
        },{
          breakpoint: 500,
          options: {
            chart: {
        width: 200,
            }
          }
        }]
        };

        var chart = new ApexCharts(document.querySelector("#chart124"), options);
        chart.render();

        $('.owl-carousel').owlCarousel({
      loop: true,
      margin: 0,
      responsiveClass: true,
      autoplay: true,
      dots: false,
      nav: true,
      responsive: {
        0: {
        items: 1,
        },
        600: {
        items: 1,
        },
        1000: {
        items: 1,
        }
      }
      });



            var datepaginator = function() {
      return {
        init: function() {
          $("#paginator1").datepaginator()
        }
      }
    }();
    jQuery(document).ready(function() {
      datepaginator.init()
    });
  
  
    $('.inner-user-div4').slimScroll({
      height: '312px'
      });
  
    $('.inner-user-div3').slimScroll({
      height: '127px'
      });



    var options = {
          series: [{
          data: [47, 45, 54, 38, 56, 24, 65, 31, 37, 39, 62, 51, 35, 41, 35, 27, 93, 53, 61, 27, 54, 43, 19, 46]
        }],
          chart: {
          type: 'line',
          height: 50,
          sparkline: {
            enabled: true
          },
        },
        stroke: {
          curve: 'smooth',
      width: 2,
        },
        fill: {
          opacity: 1,
        },
        colors: ['#0d6efd'],
        xaxis: {
          RubyXhairs: {
            width: 1
          },
        },
        yaxis: {
          min: 0
        },
        };
    var chart = new ApexCharts(document.querySelector("#expenses-spark"), options);
        chart.render();


    var options = {
      series: [{
      data: [12, 14, 2, 47, 32, 44, 14, 55, 41, 69]
    }],
      chart: {
      type: 'bar',
      height: 46.5,
      sparkline: {
        enabled: true
      },
    },
    stroke: {
      curve: 'smooth',
  width: 10,
    },
    fill: {
      opacity: 1,
    },
    colors: ['#0d6efd'],
    };

    var chart = new ApexCharts(document.querySelector("#spark2"), options);
    chart.render();



    var options = {
        series: [{
        data: [47, 45, 54, 38, 56, 24, 65, 31, 37, 39, 62, 51, 35, 41, 35, 27, 93, 53, 61, 27, 54, 43, 19, 46]
      }],
        chart: {
        type: 'bar',
        height: 50,
        sparkline: {
          enabled: true
        },
      },
      stroke: {
        curve: 'straight'
      },
      fill: {
        opacity: 1,
      },
      colors: ['#0d6efd'],
      xaxis: {
        RubyXhairs: {
          width: 5


        },
      },
      yaxis: {
        min: 0
      },
      };
  var chart = new ApexCharts(document.querySelector("#profit-spark"), options);
      chart.render();





      var options = {
          series: [{
            name: 'Male',
            data: [20, 15, 10, 25, 30, 30, 25,]
          },{
              name: 'Female',
              data: [30, 20, 35, 30, 40, 36, 32,]
            }],
          chart: {
          height: 287,
          type: 'bar',
          toolbar: {
            show: false
          },
        },
        plotOptions: {
          bar: {
            borderRadius: 5,  
            dataLabels: {
              position: 'top', // top, center, bottom
            },
          }
        },
        colors: ["#209dff" , "#1dbfc1"],
        stroke: {
          show: true,
          width: 4,
          colors: ['transparent']
        },
        dataLabels: {
          enabled: false,
          formatter: function (val) {
            return val + "%";
          },
          offsetY: -20,
          style: {
            fontSize: '12px',
            colors: ["#304758" , "#FFAA05"]
          }
        },
        legend: {
            show: false,
        },
        grid: {
            show: false,
          },
        xaxis: {
          categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          position: 'bottom',
          labels: {
            show: true,
          },
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false
          },
          crosshairs: {
            fill: {
              type: 'gradient',
              gradient: {
                colorFrom: '#D8E3F0',
                colorTo: '#BED1E6',
                stops: [0, 100],
                opacityFrom: 0.4,
                opacityTo: 0.5,
              }
            }
          },
          tooltip: {
            enabled: false,
          }
        },
        yaxis: {
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false,
          },
          labels: {
            show: false,
            formatter: function (val) {
              return val + "%";
            }
          }
        
        },
        title: {
          text: '',
          floating: false,
          offsetY: 330,
          align: 'center',
          style: {
            color: '#444'
          }
        },
        responsive: [{
            breakpoint: 1367,
            options: {          
                chart: {
                height: 235,
              },
            },
          },
          {
            breakpoint: 1195,
            options: {          
                chart: {
                height: 235,
              },
            },
          }
          ],

        };

        var chart = new ApexCharts(document.querySelector("#Reality-chart"), options);
        chart.render();

        var options = {
          series: [{
            name: 'Patient In',
            data: [30, 45, 30, 30, 10, 15, 45,]
          },
          {
            name: 'Patient Out',
            data: [40, 22, 30, 35, 15, 20, 35,]
          },
          {
            name: 'Discharged',
            data: [10, 35, 20, 25, 30, 25, 33,]
          },
          {
            name: 'Emergency',
            data: [20, 25, 25, 12, 25, 13, 25,]
          }],
          chart: {
          height: 267,
          type: 'bar',
          toolbar: {
            show: false
          },
        },
        plotOptions: {
          bar: {
            borderRadius: 5,  
            dataLabels: {
              position: 'top', // top, center, bottom
            },
          }
        },
        colors: ["#fc696a", "#0d6efd","#209dff", "#ff9920"],
        stroke: {
          show: true,
          width: 4,
          colors: ['transparent']
        },
        dataLabels: {
          enabled: false,
          formatter: function (val) {
            return val + "%";
          },
          offsetY: -20,
          style: {
            fontSize: '12px',
            colors: ["#304758" , "#FFAA05"]
          }
        },
        legend: {
            show: false,
        },
        grid: {
            show: false,
          },
        xaxis: {
          categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          position: 'bottom',
          labels: {
            show: true,
          },
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false
          },
          crosshairs: {
            fill: {
              type: 'gradient',
              gradient: {
                colorFrom: '#D8E3F0',
                colorTo: '#BED1E6',
                stops: [0, 100],
                opacityFrom: 0.4,
                opacityTo: 0.5,
              }
            }
          },
          tooltip: {
            enabled: false,
          }
        },
        yaxis: {
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false,
          },
          labels: {
            show: false,
            formatter: function (val) {
              return val + "%";
            }
          }
        
        },
        title: {
          text: '',
          floating: false,
          offsetY: 330,
          align: 'center',
          style: {
            color: '#444'
          }
        }
        };

        var chart = new ApexCharts(document.querySelector("#Reality-chart-2"), options);
        chart.render();



         var options = {
          series: [{
            name: "Desktops",
            data: [10, 41, 35, 51, 49, 62, 69, 91, 120],
            
        }],
          chart: {
          height: 150,
          type: 'line',
          toolbar:{
              show: false
            },
          zoom: {
            enabled: false
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight'
        },
        grid: {
          borderColor: '#f7f7f7',
          row: {
            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
            opacity: 0.1
          },
        },
        xaxis: {
          categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep'],
          position: 'bottom',
          labels: {
            show: false,
          },
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false
          },
        },
        yaxis: {
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false,
          },
          labels: {
            show: false,
            formatter: function (val) {
              return val + "%";
            }
          }
        
        },
        responsive: [{
            breakpoint: 1441,
            options: {          
                chart: {
                height: 130,
              },
            },
          },
          {
            breakpoint: 1367,
            options: {          
                chart: {
                height: 154,
              },
            },
          },
          {
            breakpoint: 1195,
            options: {          
                chart: {
                height: 132,
              },
            },
          }
          ],

        };

        var chart = new ApexCharts(document.querySelector("#successa-rate"), options);
        chart.render();



         var options = {
          series: [{
          name: 'Series 1',
          data: [30, 70, 50, 89, 97, 100],
        }],
          chart: {
          height: 325,
          type: 'radar',
          toolbar:{
            show: false,
          },
        },
        colors: ['#1dbfc1'],
        yaxis: {
          stepSize: 10
        },
        xaxis: {
          categories: ['Anesthtics', 'Gynecology', 'Nerology', 'Oncology', 'Orthopedics', 'Physiotherapy']
        }
        };

        var chart = new ApexCharts(document.querySelector("#consultation-chart"), options);
        chart.render();


            var options = {
          series: [
          {
            name: "Heart",
      data: [12, 22, 14, 18, 22 , 13, 17]
          },
          {
            name: "Fracture",            
            data: [28, 39, 23, 36, 45, 32, 43]
          },
          {
            name: "Cold",            
            data: [17, 12, 28, 12, 33, 26, 23]
          }
        ],
          chart: {
          height: 260,
          type: 'line',
      foreColor:"#bac0c7",
          dropShadow: {
            enabled: true,
            color: '#000',
            top: 18,
            left: 7,
            blur: 10,
            opacity: 0.2
          },
          toolbar: {
            show: false
          }
        },
        colors: ['#5156be', '#da123b', '#ffa800'],
        dataLabels: {
          enabled: false,
        },
        stroke: {
          curve: 'smooth'
        },
        grid: {
          borderColor: '#e7e7e7',
        },
        xaxis: {
          categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
        },
        legend: {
          show: true,
      position: 'top',
        }
        };

        var chart = new ApexCharts(document.querySelector("#recovery_statistics"), options);
        chart.render();


     
  });



// slimScroll-------------------------------------------------
window.onload = function() {
  // Cache DOM Element
  var scrollable = $('.scrollable');
  
  // Keeping the Scrollable state separate
  var state = {
    pos: {
      lowest: 0,
      current: 0
    },
    offset: {
      top: [0, 0], //Old Offset, New Offset
    }
  }
  //
  scrollable.slimScroll({
    height: '300px',
    width: '',
    start: 'top'
  });
  //
  scrollable.slimScroll().bind('slimscrolling', function (e, pos) {
    // Update the Scroll Position and Offset
    
    // Highest Position
    state.pos.highest = pos !== state.pos.highest ?
      pos > state.pos.highest ? pos : state.pos.highest
    : state.pos.highest;
    
    // Update Offset State
    state.offset.top.push(pos - state.pos.lowest);
    state.offset.top.shift();
    
    if (state.offset.top[0] < state.offset.top[1]) {
      console.log('...Scrolling Down')
      // ... YOUR CODE ...
    } else if (state.offset.top[1] < state.offset.top[0]) {
      console.log('Scrolling Up...')
      // ... YOUR CODE ...
    } else {
      console.log('Not Scrolling')
      // ... YOUR CODE ...
    }
  });
};

window.onload = function() {
  // Cache DOM Element
  var scrollable = $('.scrollable');
  
  // Keeping the Scrollable state separate
  var state = {
    pos: {
      lowest: 0,
      current: 0
    },
    offset: {
      top: [0, 0], //Old Offset, New Offset
    }
  }
  //
  scrollable.slimScroll({
    height: '300px',
    width: '',
    start: 'top'
  });
  //
  scrollable.slimScroll().bind('slimscrolling', function (e, pos) {
    // Update the Scroll Position and Offset
    
    // Highest Position
    state.pos.highest = pos !== state.pos.highest ?
      pos > state.pos.highest ? pos : state.pos.highest
    : state.pos.highest;
    
    // Update Offset State
    state.offset.top.push(pos - state.pos.lowest);
    state.offset.top.shift();
    
    if (state.offset.top[0] < state.offset.top[1]) {
      console.log('...Scrolling Down')
      // ... YOUR CODE ...
    } else if (state.offset.top[1] < state.offset.top[0]) {
      console.log('Scrolling Up...')
      // ... YOUR CODE ...
    } else {
      console.log('Not Scrolling')
      // ... YOUR CODE ...
    }
  });
};

// slimScroll------------------------------------------------- End
