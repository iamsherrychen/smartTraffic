class chart_filter {
    constructor( time_point, data, speed_east, speed_west, travel_east, travel_west, delay, choose, which_chart) {
        this.time_point = time_point;
        this.data = data;
        this.speed_east = speed_east;
        this.speed_west = speed_west;
        this.travel_east = travel_east;
        this.travel_west = travel_west;
        this.delay = delay;
        this.choose = choose;
        this.which_chart = which_chart;
    }

    start_print() {
        return (this.choose === "trafficTrends" && this.trends()) ||
        (this.choose === "travelTime" && this.travel()) || 
        (this.choose === "stalling" && this.stalling())
    }

    trends() {
        let options;
        if(this.which_chart === "normal_chart"){
            this.speed_east.unshift('速度-東向');
            this.data.push(this.speed_east);
            this.speed_west.unshift('速度-西向');
            this.data.push(this.speed_west);
            // $("g.c3-shapes-速度-東向 path", "g.c3-shapes-速度-西向 path").css({"stroke-width": "30"})
            this.time_point.unshift("x");
            this.data.unshift(this.time_point);
            options = {
                size: {
                    height: 557,
                    width: 1400
                },
                legend: {
                    show: true,
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 10,
                        y: -40,
                        step: 2
                    }
                },
                padding: {
                    top: 30
                },
                data: {
                    x: 'x',
                    xFormat: '%H',
                    columns: this.data,
                    axes: {
                        // data1: 'y',
                        '速度-東向': 'y2',
                        '速度-西向':'y2'
                    },
                    type: 'bar',
                    types: {
                        '速度-東向': 'line',
                        '速度-西向': 'line'
                    }
                },
                subchart: {
                    show: true
                },
                grid: {
                    x: {
                        // show: true
                    },
                    y: {
                        show: true,
                        // lines: ygrid
                    }
                },
                bar: {
                    width: {
                        ratio: 0.6
                    }
                },
                line: {},
                color: {
                    pattern: ['#FFADAD', '#8D94BA', '#2CB575', '#E0D100', '#81C0C5', '#0B3C49', '#92A09C', '#32CD32', '#D58936', '#7E1811']
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        localtime: true,
                        tick: {
                            culling: false,
                            format: '%H:%M',
                        }
                        // label: {
                        //     text: '時間',
                        // 	position: 'outer-center'
                        // }
                    },
                    y: {
                        label: {
                            text: 'PCU',
                            position: 'outer-middle'
                        },
                        padding: {
                            top: 100,
                            bottom: 0
                        }
                    },
                    y2: {
                        show: true,
                        label: {
                            text: '速率 (km/h)',
                            position: 'outer-middle'
                        }
                    }
                }
            }
        }else{
            this.data = [];
            this.speed_east.unshift('速度-東向');
            this.data.push(this.speed_east);
            this.speed_west.unshift('速度-西向');
            this.data.push(this.speed_west);
            // $("g.c3-shapes-速度-東向 path", "g.c3-shapes-速度-西向 path").css({"stroke-width": "30"})
            this.time_point.unshift("x");
            this.data.unshift(this.time_point);
            options = {
                size: {
                    height: 557,
                    width: 1400
                },
                legend: {
                    show: true,
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 10,
                        y: -40,
                        step: 2
                    }
                },
                padding: {
                    top: 30
                },
                data: {
                    x: 'x',
                    xFormat: '%H',
                    columns: this.data,
                    axes: {
                        // data1: 'y',
                        '速度-東向': 'y',
                        '速度-西向':'y'
                    },
                    type: 'bar',
                    types: {
                        '速度-東向': 'line',
                        '速度-西向': 'line'
                    }
                },
                subchart: {
                    show: true
                },
                grid: {
                    x: {
                        // show: true
                    },
                    y: {
                        show: true,
                        // lines: ygrid
                    }
                },
                bar: {
                    width: {
                        ratio: 0.6
                    }
                },
                line: {},
                color: {
                    pattern: ['#FFADAD', '#8D94BA', '#2CB575', '#E0D100', '#81C0C5', '#0B3C49', '#92A09C', '#32CD32', '#D58936', '#7E1811']
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        localtime: true,
                        tick: {
                            culling: false,
                            format: '%H:%M',
                        }
                    },
                    y: {
                        label: {
                            text: '速率 (km/h)',
                            position: 'outer-middle'
                        }
                    }
                }
            }
        }
        return options
    }

    travel(){
        let travel_data = []
        this.travel_east.unshift('東向');
        travel_data.push(this.travel_east);

        this.travel_west.unshift('西向');
        travel_data.push(this.travel_west);

        this.time_point.unshift("x");
        travel_data.unshift(this.time_point);

        let options = {
            size: {
                height: 557,
                width: 1400
            },
            legend: {
                show: true,
                position: 'inset',
                inset: {
                    anchor: 'top-left',
                    x: 10,
                    y: -40,
                    step: 2
                }
            },
            padding: {
                top: 40
            },
            data: {
                x: 'x',
                xFormat: '%H',
                columns: travel_data,
                axes: {
                    // data1: 'y',
                    '東向': 'y',
                    '西向':'y'
                },
                // type: 'bar',
                types: {
                    '東向': 'line',
                    '西向': 'line'
                }
            },
            subchart: {
                show: true
            },
            grid: {
                x: {
                    // show: true
                },
                y: {
                    show: true,
                    // lines: ygrid
                }
            },
            line: {},
            bar: {
                width: {
                    ratio: 0.6
                }
            },
            color: {
                pattern: ['#FFADAD', '#8D94BA', '#2CB575', '#E0D100', '#81C0C5', '#0B3C49', '#92A09C', '#32CD32', '#D58936', '#7E1811']
            },
            axis: {
                x: {
                    type: 'timeseries',
                    localtime: true,
                    tick: {
                        culling: false,
                        format: '%H:%M',
                    }
                },
                y: {
                    label: {
                        text: 'Travel Time',
                        position: 'outer-middle'
                    },
                    padding: {
                        top: 100,
                        bottom: 0
                    }
                }
            }
        }
        return options
    }

    stalling() {
        this.time_point.unshift("x");
        this.delay.unshift(this.time_point);

        let options = {
            size: {
                height: 557,
                width: 1400
            },
            legend: {
                show: true,
                position: 'inset',
                inset: {
                    anchor: 'top-left',
                    x: 10,
                    y: -40,
                    step: 2
                }
            },
            padding: {
                top: 40
            },
            data: {
                x: 'x',
                xFormat: '%H',
                columns: this.delay,
                axes: {
                    // data1: 'y',
                    '速度-東向': 'y2',
                    '速度-西向':'y2'
                },
                type: 'bar',
                types: {
                    '速度-東向': 'line',
                    '速度-西向': 'line'
                }
            },
            subchart: {
                show: true
            },
            grid: {
                x: {
                    // show: true
                },
                y: {
                    show: true,
                    // lines: ygrid
                }
            },
            bar: {
                width: {
                    ratio: 0.6
                }
            },
            line: {},
            color: {
                pattern: ['#FFADAD', '#8D94BA', '#2CB575', '#E0D100', '#81C0C5', '#0B3C49', '#92A09C', '#32CD32', '#D58936', '#7E1811']
            },
            axis: {
                x: {
                    type: 'timeseries',
                    localtime: true,
                    tick: {
                        culling: false,
                        format: '%H:%M',
                    }
                },
                y: {
                    label: {
                        text: 'car-delay',
                        position: 'outer-middle'
                    },
                    padding: {
                        top: 100,
                        bottom: 0
                    }
                },
            }
        }
        return options
    }
}