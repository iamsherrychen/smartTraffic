window.env = {
    isDev: false,
    getWsUrl: function() {
        console.log(this.isDev, "======");
        if (this.isDev) {
            // return 'wss://localhost/traffic';
            return 'wss://pixord.wiadvancelabs.com/traffic';
            
        } else {
            return 'wss://' + document.domain + '/traffic';
        }
    },
    getOsrmUrl: function() {
        if (this.isDev) {
            // return 'https://localhost/osrm';
            return 'https://pixord.wiadvancelabs.com/osrm';
        } else {
            return 'https://' + document.domain + '/osrm';
        }
    },
    getVideoUrl: function(project) {
        return project === "dayuan" ? 'http://' + document.domain + '/Dayuan/' : 'http://' + document.domain + '/Dazhu/';
    }
}