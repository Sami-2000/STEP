google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawCharts);
 

function drawCharts() {
  drawStaticPieChart();
  drawLineChart();
  drawBarChart();
}

/*Creates interactive bar chart saying if I'm cool.*/
function drawBarChart() {
  fetch('/cool-data').then(response => response.json())
  .then((coolVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Cool?');
    data.addColumn('number', 'Votes');
    Object.keys(coolVotes).forEach((coolCategory) => {
      data.addRow([coolCategory, coolVotes[coolCategory]]);
    });

    const options = {
      'title': 'Am I Cool?',
      'width':800,
      'height':1200
    };

    const chart = new google.visualization.ColumnChart(
        document.getElementById('bar-chart-container'));
    chart.draw(data, options);
  });
}

/* Creates a line chart of women on Fortune 500 boards and adds it to the page. */
function drawLineChart() {
  fetch('/chart-data').then(response => response.json())
  .then((percentRepresentation) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', 'Percent Female');
    Object.keys(percentRepresentation).forEach((year) => {
      data.addRow([year, percentRepresentation[year]]);
    });

    const options = {
      'title': 'Female Representation on Fortune 500 Company Boards',
      'width':800,
      'height':1200
    };

    const chart = new google.visualization.LineChart(
        document.getElementById('line-chart-container'));
    chart.draw(data, options);
  });
}

/* Creates a pie chart of quarentine activities and adds it to the page. */
function drawStaticPieChart() {
  const data = google.visualization.arrayToDataTable([
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
 
  const options = {
    title: 'How I Spend a Week in Quarentine',
      is3D: true,
      'width':800,
      'height':1200,
    };
 
  const chart = new google.visualization.PieChart(
      document.getElementById('pie-chart-container'));
  chart.draw(data, options);
}
