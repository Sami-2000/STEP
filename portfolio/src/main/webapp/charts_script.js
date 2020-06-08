google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawStaticPieChart);
 
/** Creates a chart and adds it to the page. */
function drawStaticPieChart() {
  var data = google.visualization.arrayToDataTable([
    ['Activity', 'Hours per Week'],
    ['Internship',     40],
    ['Baking',      6],
    ['Eating',  14],
    ['Playing Games',  8],
    ['Watching TV', 10],
    ['Sleeping',    60],
    ['Calling Friends',  6],
    ['Reading', 12],
    ['Walking', 6],
    ['Other', 6],
    ['Seeing Friends',  0]
  ]);
 
  var options = {
    title: 'How I Spend a Week in Quarentine',
      is3D: true,
      'width':800,
      'height':1200,
    };
 
  const chart = new google.visualization.PieChart(
      document.getElementById('pie-chart-container'));
  chart.draw(data, options);
}
