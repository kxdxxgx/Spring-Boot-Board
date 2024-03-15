// 쿠키 가져 오기 
const getCookie = (name) => {
	
	const cookies = document.cookie.split(`; `).map((el)=>el.split('='));
	let getItem = [];
	console.log('cookies = ' + cookies);
	for(let i=0; i<cookies.length; i++) {
		if(cookies[i][0] == name) { 
			// name: userid, getItem = ['jingom368']
			// name: password, getItem = ['V1RGa05HVkhSblZVV0doUFZrZHpPUT09']
			getItem.push(cookies[i][1]);
			break;
		}	
	}
	console.log(getItem)
		
	if(getItem.length>0) { // 한글을 쿠키값으로 쓸 때 : %20%30
		return decodeURIComponent(getItem[0]);
	}
}