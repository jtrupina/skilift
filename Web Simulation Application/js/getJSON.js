/**
 * Created by jtrupina on 11.11.2014..
 */

$(document).ready(function() {

    var options = {
        chart: {
            renderTo: 'container',
            type: 'column'
        },
        title: {
            text: 'Result of the simulation'
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: 'Percentage'
            }
        },

        series: [{
            name: 'Lifts',
            data: []

        }]
    };
    
    var percentages = [];
    var lifts = [];
    var status = [];
    
    $.getJSON('simulations.json', function (data) {
            refreshData(data);
            createGraph();
            liftInfo();
        });
    
   
    
    $("body").on("click","[name='btn_low'],[name='btn_med'],[name='btn_high']",function(evt){
        var index = $(this).attr('data-index');
        if($(this).attr('name') === 'btn_low'){
            liftPercentage = calculatePercentage(1);
        }
        else if($(this).attr('name') === 'btn_med'){
            liftPercentage = calculatePercentage(2);
        }
        else if($(this).attr('name') === 'btn_high'){
            liftPercentage = calculatePercentage(3);
        }
        
        $.ajax({
                type:"POST",
                url:"index.php",
                dataType:"json",
                data : 
                    {
                     'lift' : index, 
                     'liftPercentage' : liftPercentage
                     },
                success:function(result){
                    percentages[index] = liftPercentage;
                    //sortArrayByPercentage();
                    createGraph();
                    liftInfo();
                }
            
            
        }); //end of ajax transmission
        
    }); //setting lift to  low traffic
    
    
    $("body").on("click","[name='btn_turn_off'],[name='btn_turn_on']",function(evt){
        var index = $(this).attr('data-index');
        switchStatus(index);
        $.ajax({
                type:"GET",
                url:"index.php",
                dataType:"json",
                data : 
                    {
                     'lift' : index, 
                     'mode' : status[index]
                     },
                success:function(result){
                    
                    //sortArrayByPercentage();
                    createGraph();
                    liftInfo();
                },
                error:function(result){
                  switchStatus(index);  
                }
    }); //kraj ajaxa
    });//kraj dogadaja
    
    function calculatePercentage(liftPercentage){
        
        if(liftPercentage ===1 ){
            return Math.floor(Math.random()*(25-0+1)+0);
        }
        else if(liftPercentage === 2){
            return Math.floor(Math.random()*(75-25+1)+25);
        }
        else if(liftPercentage === 3){
            return Math.floor(Math.random()*(100-75+1)+75);
        }
        
    }
    
    function switchStatus(index){
        if(status[index] === 1){
            percentages[index] = 200;
            status[index] = 0;
        }
        else{
            status[index] = 1;
            percentages[index] = 0;
        }
    }
    
    function liftInfo(){
        
        $('#lift').empty();
        
       $.each(lifts, function( index ) {
            if(status[index] === 1){
                $list_lifts = "<p>The current capacity of lift  " + lifts[index] + " is " + percentages[index] + 
                       "<button type='button' name='btn_low' class='btn btn-success' data-index='"+index+"'> Low </button>"+
                       "<button type='button' name='btn_med' class='btn btn-warning' data-index='"+index+"'> Med </button>"+
                       "<button type='button' name='btn_high' class='btn btn-danger' data-index='"+index+"'> High </button>"+ 
                       "<button type='button' name='btn_turn_off' class='btn btn-danger' data-index='"+index+"'>Turn off</button></p>";
     
            }
            else{

                $list_lifts = "<p>" + lifts[index] + " is off  <button type='button' name='btn_turn_on' class='btn btn-success' data-index='"+index+"'>Turn on</button></p>";
            }
                
                $('#lift').append($list_lifts);
            });
         
    }//1-radi
    
    function refreshData(data){
           
                $.each(data, function (i,item) {
                    lifts[i] = data[i].name;
                    percentages[i] = data[i].percentage;
                    status[i] = data[i].status;

            });
        
            } //end of refreshData function
            
            
    function createGraph(){
        $('#container').empty();
        
        var lifts_help = [];
        var percentages_help = [];
        var i = 0;
        $.each(lifts, function( index ){
            
            if(status[index]  === 1){
                lifts_help[i]=lifts[index];
                percentages_help[i]=percentages[index];
                i++;
            }
        });
        
        
        options.xAxis.categories = lifts_help;
        options.series[0].data = percentages_help;
        //options.series[0].setData(proba);
        var chart = new Highcharts.Chart(options);
    }//end of createGraph function
    
    function sortArrayByPercentage(){
        
        var j ;
        var flag = true;
        var temp_percentage;
        var temp_lift;
        var temp_status;
        
        while(flag){
            flag = false;
            for(j = 0;j < percentages.length - 1;j++){
               
                if(percentages[j] > percentages[j+1]){

                    temp_percentage = percentages[j];
                    percentages[j] = percentages[j+1];
                    percentages[j+1] = temp_percentage;
                    
                    temp_lift = lifts[j];
                    lifts[j] = lifts[j+1];
                    lifts[j+1] = temp_lift;

                    temp_status = status[j];
                    status[j] = status[j+1];
                    status[j+1] = temp_status;

                    flag = true;
                }//ako se radi o većem
                
               
            }//kraj fora
            
            
        }//while

    } // kraj sorta
    
});//end of document