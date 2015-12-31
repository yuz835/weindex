function showNav(id) {
	for (var i = 1; i <= 3; i++) {
		if (i == id) {
			$("#nav_" + i).attr("class", "NavActive");
			$("#menu_" + i).css("display", "block");
		} else {
			$("#nav_" + i).attr("class", "NavUnactive");
			$("#menu_" + i).css("display", "none");
		}
	}
}
