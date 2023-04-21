function Postcode() {
        new daum.Postcode({
            oncomplete: function(data) {
            	console.log(data.zonecode)
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
 
                // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 도로명 조합형 주소 변수
 
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }
                // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
                if(fullRoadAddr !== ''){
                    fullRoadAddr += extraRoadAddr;
                }
 
                
                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                $('#post1').val(data.zonecode);
                $('#address').val(fullRoadAddr);
     
            }
        }).open();
    }//function Postcode()
    
    
    

$(function(){

	let is_idcheck_click = false;
	let idcheck_value ='';
	

	$('#idcheck').click(function(){
		const id = $('#id');
		const id_value = $('#id').val().trim();
		if(id_value == ''){
			alert('아이디를 입력하세요');
			id.focus();
			return false;
	
		}else{
			pattern = /^[A-Z][a-zA-Z_0-9]{3,}$/

			if(pattern.test(id_value)){
				idcheck_value = id_value;
				is_idcheck_click = true;
				const ref = `idcheck?id=${id_value}`;
				window.open( ref,'idcheck', 'width=300 , height=250');

			}else{
				alert('첫 글자는 대문자이고 두번째부터는 대소문자, 숫자, _로 총 4개 이상이어야합니다');
				id.val('').focus();
			}
		}


	})





	$('#myform').submit(function(e){
			const id = $('#id');
			const pass = $('#pass');
			const jumin1 = $('#jumin1');
			const jumin2 = $('#jumin2');
			const post1 = $('#post1');
			const address = $('#address');
			const intro = $('#intro');
			const email = $('#email');
			const domain = $('#domain');


			
			
			if(id.val().trim() == ''){
				alert('아이디를 입력하세요');
				id.focus();
				return false;
		
			}
			
			
			if(!$("#id").prop('readonly')){  //회원가입 폼과 정보 수정폼에서 동시에 사용할 js
											// 회원가입 폼에서만사용할 문장
											 // 정보 수정 폼에서는 아이디를 수정하지 않기때문에 필요없는 부분
				console.log($("#id").prop('readonly')); 
				const submit_id_value = $.trim(id.val());
				if(!is_idcheck_click || submit_id_value != idcheck_value){ //submit 당시 아이디값과 아이디 중복검사에 사용된 아이디를 비교
			
					alert('아이디 중복검사를 하세요');
					return false;
				}
				
				const result = $("#result").val();
				if(result == -1){
					alert("사용 가능한 아이디로 다시 입력하세요");
					$("#id").val('').focus();
					return false;
				}
				
			}


			if(pass.val().trim() == ''){
		
				alert('비밀번호를 입력하세요');
				pass.focus();
				return false;
			
			}

	



			if(jumin1.val().trim()  == ''){
				
				alert('주민번호 앞지리를 입력하세요');
				jumin1.focus();
				return false;
			
			}
			if(jumin1.val().trim().length != 6){
				alert('앞자리는 6자리입니다');
				$('#jumin1').text('');
				$('#jumin1').focus();
				return false;
			}






			if(jumin2.val().trim()  == ''){
				alert('주민번호 뒷지리를 입력하세요');
				jumin2.focus();
				return false;
			}
			if(jumin2.val().trim().length != 7){
				alert('뒷자리는 7자리입니다');
				$('#jumin2').text('');
				$('#jumin2').focus();
				return false;
			}







			if(email.val().trim()  == ''){
				
				alert('이메일을 입력하세요');
				email.focus();
				return false;
			
			}
			if(domain.val().trim()  == ''){
				
				alert('도메인을 입력하세요');
				domain.focus();
				return false;
			
			}


			if( $('input:radio:checked').length == 0){
				alert('성별을 선택하세요');	
				return false;
			}


			
			if( $('input:checkbox[name=hobby]:checked').length < 2){
				alert('취미는 2개이상을 선택하세요');	
				return false;
			}






			if(post1.val().trim()  == ''){
				
				alert('우편번호를 입력하세요');
				post1.focus();
				return false;
			
			}

			if(!$.isNumeric(post1.val())){
				alert('우편번호는 숫자만 입력 가능합니다.');
				post1.val('').focus();
				return false;
			
			}


			if(address.val().trim()  == ''){
				
				alert('주소를 입력하세요');
				address.focus();
				return false;
			
			}

			

			if(intro.val().trim()  == ''){
				
				alert('자기소개를 입력하세요');
				intro.focus();
				return false;
			
			}



		})


	
	
	


	$('#sel').change(function(){
	
		var sel = $('#sel');
		var domain = $('#domain');
		if (sel.val() =='')
		{
			domain.attr('readonly', false);
			domain.val('').focus();
		}else{
			domain.val(sel.val())  ;
			domain.prop('readonly', true);
		}

	})

			

	$('#jumin1').keyup(function(){
		if($(this).val().trim().length==6){
			var pattern = /^[0-9]{2}(0[1-9]|1[012])(0[1-9]|1[0-9]|2[0-9]|3[01])$/ ;
			if(pattern.test($(this).val())){
					$('#jumin2').focus()
			}else{
					alert('형식에 맞게 입력하세요');
					$(this).val('').focus();
			}
			
		}
		
	})

	

	$('#jumin2').keyup(function(){
		var pattern = /^[1234][0-9]{6}$/ ;
		if($(this).val().trim().length==7){
			if(pattern.test($('#jumin2').val().trim())){
				const c = $(this).val().substr(0,1);
				const index = (c-1) %2;
				$('input[type=radio]').eq(index).prop('checked',true);

			}else{
				alert('형식에 맞게 입력하세요');
				$(this).val('').focus();
			}
		}

		
	})









})