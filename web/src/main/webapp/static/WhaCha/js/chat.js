var KEY_ENTER=13;
$(document).ready(function(){
	var $input=$(".chat-input")
		,$sendButton=$(".chat-send")
		,$messagesContainer=$(".chat-messages")
		,$messagesList=$(".chat-messages-list")
		,$effectContainer=$(".chat-effect-container")
		,$infoContainer=$(".chat-info-container")

		,messages=0
		,bleeding=100
		,isFriendTyping=false
		,incomingMessages=0
		,lastMessage=""
	;
	
	var lipsum="Hello, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	function gooOn(){
		setFilter('url(#goo)');
	}
	function gooOff(){
		setFilter('none');
	}
	function setFilter(value){
		$effectContainer.css({
			webkitFilter:value,
			mozFilter:value,
			filter:value,
		});
	}

	function addMessage(message,self){
		var $messageContainer=$("<li/>")
			.addClass('chat-message '+(self?'chat-message-self':'chat-message-friend'))
			.appendTo($messagesList)
		;
		var $messageBubble=$("<div/>")
			.addClass('chat-message-bubble')
			.appendTo($messageContainer)
		;
		$messageBubble.text(message);

		var oldScroll=$messagesContainer.scrollTop();
		$messagesContainer.scrollTop(9999999);
		var newScroll=$messagesContainer.scrollTop();
		var scrollDiff=newScroll-oldScroll;
		TweenMax.fromTo(
			$messagesList,0.4,{
				y:scrollDiff
			},{
				y:0,
				ease:Quint.easeOut
			}
		);

		return {
			$container:$messageContainer,
			$bubble:$messageBubble
		};
	}
	function uiSendMessage(){
		var message=$input.text();
		
		if(message=="") return;
		
		lastMessage=message;

		var messageElements=addMessage(message,true)
			,$messageContainer=messageElements.$container
			,$messageBubble=messageElements.$bubble
		;

		var oldInputHeight=$(".chat-input-bar").height();
		$input.text('');
		updateChatHeight();
		var newInputHeight=$(".chat-input-bar").height();
		var inputHeightDiff=newInputHeight-oldInputHeight

		var $messageEffect=$("<div/>")
			.addClass('chat-message-effect')
			.append($messageBubble.clone())
			.appendTo($effectContainer)
			.css({
				left:$input.position().left-12,
				top:$input.position().top+bleeding+inputHeightDiff
			})
		;


		var messagePos=$messageBubble.offset();
		var effectPos=$messageEffect.offset();
		var pos={
			x:messagePos.left-effectPos.left,
			y:messagePos.top-effectPos.top
		}

		var $sendIcon=$sendButton.children("i");
		TweenMax.to(
			$sendIcon,0.15,{
				x:30,
				y:-30,
				force3D:true,
				ease:Quad.easeOut,
				onComplete:function(){
					TweenMax.fromTo(
						$sendIcon,0.15,{
							x:-30,
							y:30
						},
						{
							x:0,
							y:0,
							force3D:true,
							ease:Quad.easeOut
						}
					);
				}
			}
		);

		gooOn();

		
		TweenMax.from(
			$messageBubble,0.8,{
				y:-pos.y,
				ease:Sine.easeInOut,
				force3D:true
			}
		);

		var startingScroll=$messagesContainer.scrollTop();
		var curScrollDiff=0;
		var effectYTransition;
		var setEffectYTransition=function(dest,dur,ease){
			return TweenMax.to(
				$messageEffect,dur,{
					y:dest,
					ease:ease,
					force3D:true,
					onUpdate:function(){
						var curScroll=$messagesContainer.scrollTop();
						var scrollDiff=curScroll-startingScroll;
						if(scrollDiff>0){
							curScrollDiff+=scrollDiff;
							startingScroll=curScroll;

							var time=effectYTransition.time();
							effectYTransition.kill();
							effectYTransition=setEffectYTransition(pos.y-curScrollDiff,0.8-time,Sine.easeOut);
						}
					}
				}
			);
		}

		effectYTransition=setEffectYTransition(pos.y,0.8,Sine.easeInOut);
		
		// effectYTransition.updateTo({y:800});

		TweenMax.from(
			$messageBubble,0.6,{
				delay:0.2,
				x:-pos.x,
				ease:Quad.easeInOut,
				force3D:true
			}
		);
		TweenMax.to(
			$messageEffect,0.6,{
				delay:0.2,
				x:pos.x,
				ease:Quad.easeInOut,
				force3D:true
			}
		);

		TweenMax.from(
			$messageBubble,0.2,{
				delay:0.65,
				opacity:0,
				ease:Quad.easeInOut,
				onComplete:function(){
					TweenMax.killTweensOf($messageEffect);
					$messageEffect.remove();
					if(!isFriendTyping)
						gooOff();
				}
			}
		);

		messages++;

		//if(Math.random()<0.65 || lastMessage.indexOf("?")>-1 || messages==1) getReply();
	}
	function getReply(){
		if(incomingMessages>2) return;
		incomingMessages++;
		var typeStartDelay=1000+(lastMessage.length*40)+(Math.random()*1000);
		setTimeout(friendIsTyping,typeStartDelay);

		var source=lipsum.toLowerCase();
		source=source.split(" ");
		var start=Math.round(Math.random()*(source.length-1));
		var length=Math.round(Math.random()*13)+1;
		var end=start+length;
		if(end>=source.length){
			end=source.length-1;
			length=end-start;
		}
		var message="";
		for (var i = 0; i < length; i++) {
			message+=source[start+i]+(i<length-1?" ":"");
		};
		message+=Math.random()<0.4?"?":"";
		message+=Math.random()<0.2?" :)":(Math.random()<0.2?" :(":"");

		var typeDelay=300+(message.length*50)+(Math.random()*1000);

		setTimeout(function(){
			receiveMessage(message);
		},typeDelay+typeStartDelay);

		setTimeout(function(){
			incomingMessages--;
			if(Math.random()<0.1){
				getReply();
			}
			if(incomingMessages<=0){
				friendStoppedTyping();
			}
		},typeDelay+typeStartDelay);
	}
	function friendIsTyping(){
		if(isFriendTyping) return;

		isFriendTyping=true;

		var $dots=$("<div/>")
			.addClass('chat-effect-dots')
			.css({
				top:-30+bleeding,
				left:10
			})
			.appendTo($effectContainer)
		;
		for (var i = 0; i < 3; i++) {
			var $dot=$("<div/>")
				.addClass("chat-effect-dot")
				.css({
					left:i*20
				})
				.appendTo($dots)
			;
			TweenMax.to($dot,0.3,{
				delay:-i*0.1,
				y:30,
				yoyo:true,
				repeat:-1,
				ease:Quad.easeInOut
			})
		};

		var $info=$("<div/>")
			.addClass("chat-info-typing")
			.text("Your friend is typing...")
			.css({
				transform:"translate3d(0,30px,0)"
			})
			.appendTo($infoContainer)

		TweenMax.to($info, 0.3,{
			y:0,
			force3D:true
		});

		gooOn();
	}

	function friendStoppedTyping(){
		if(!isFriendTyping) return

		isFriendTyping=false;

		var $dots=$effectContainer.find(".chat-effect-dots");
		TweenMax.to($dots,0.3,{
			y:40,
			force3D:true,
			ease:Quad.easeIn,
		});

		var $info=$infoContainer.find(".chat-info-typing");
		TweenMax.to($info,0.3,{
			y:30,
			force3D:true,
			ease:Quad.easeIn,
			onComplete:function(){
				$dots.remove();
				$info.remove();

				gooOff();
			}
		});
	}
	function receiveMessage(message){
		var messageElements=addMessage(message,false)
			,$messageContainer=messageElements.$container
			,$messageBubble=messageElements.$bubble
		;

		TweenMax.set($messageBubble,{
			transformOrigin:"60px 50%"
		})
		TweenMax.from($messageBubble,0.4,{
			scale:0,
			force3D:true,
			ease:Back.easeOut
		})
		TweenMax.from($messageBubble,0.4,{
			x:-100,
			force3D:true,
			ease:Quint.easeOut
		})
	}

	function updateChatHeight(){
		$messagesContainer.css({
			height:666-$(".chat-input-bar").height()
		});
	}

	$input.keydown(function(event) {
		if(event.keyCode==KEY_ENTER){
			event.preventDefault();
			sendMessage();
		}
	});
	$sendButton.click(function(event){
		event.preventDefault();
		sendMessage();
		// $input.focus();
	});
	$sendButton.on("touchstart",function(event){
		event.preventDefault();
		sendMessage();
		// $input.focus();
	});

	$input.on("input",function(){
		updateChatHeight();
	});

	gooOff();
	updateChatHeight();
	
	
	
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	//TODO---------------------------------------> Ditesh Logic
	            var host = 'whacha:8080'
                
                var conversationTileTemplate = '<li><a class="gn-icon gn-icon-pictures">${phone}</a></li>'
                var messageTileTemplateInbound = '<li class="chat-message chat-message-friend"><div class="chat-message-bubble">${message}</div></li>'
                var messageTileTemplateOutbound = ''
                
                var formatTime = function(unixTimestamp) {
                    var dt = new Date(unixTimestamp);
    
                    var hours = dt.getHours();
                    var minutes = dt.getMinutes();
                    var seconds = dt.getSeconds();
    
                    // the above dt.get...() functions return a single digit
                    // so I prepend the zero here when needed
                    if (hours < 10)
                     hours = '0' + hours;
    
                    if (minutes < 10)
                     minutes = '0' + minutes;
    
                    if (seconds < 10)
                     seconds = '0' + seconds;
    
                    return hours + ":" + minutes + ":" + seconds;
                }

                function lockConversation(phone){
                   var ret = false;
                   $.ajax({
                                             url: 'http://'+host+'/lockConversation/'+phone,
                                             error: function() {
                                                alert('http://'+host+'/lockConversation/'+phone);
                                             },
                                             dataType: 'json',
                                             success: function(resp) {
                                              ret = resp
                                            },
                                             type: 'GET',
                                             async:false
                                          });
                   return ret;
                }

                function unlockConversation(phone){
                   var ret = false;
                   $.ajax({
                                             url: 'http://'+host+'/unlockConversation/'+phone,
                                             error: function() {
                                                alert('http://'+host+'/unlockConversation/'+phone);
                                             },
                                             dataType: 'json',
                                             success: function(resp) {
                                              ret = resp
                                            },
                                             type: 'GET',
                                             async:false
                                          });
                   return ret;
                }
    
                function closeCurrentChat(){
                    $('.chat-window').data('isChatOpen',false)
                    $('.chat-window').data('lastMessageIndex',-1)
                    $('.chat-window').find('.chat-messages-list').empty()
                    unlockConversation($('.chat-window').data('phone'))
                    $('.chat-window').data('phone','')
                }
    
                function logicAddMessage(message){
                    //var templateString = messageTileTemplateInbound
                    if (!message.inboundmsg){
                        //templateString = messageTileTemplateOutbound
                        addMessage(message.message,true)
                    }else {
                        addMessage(message.message,false)
                    }
                    //var template = $(templateString.replace('${message}',message.message));
                    //$('.chat-messages-list').append(template)
                    //template.data('messageMeta',message)
                }
    
                function fillGapMessages(phone,startOffset,endOffset){
                    if (startOffset != endOffset) {
                        $.ajax({
                          url: 'http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset,
                          error: function() {
                             alert('http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset);
                          },
                          dataType: 'json',
                          success: function(resp) {
                            $.each(resp.messages, function(index, message){
                                logicAddMessage(message)
                            })
                         },
                          type: 'GET'
                       });
                    }
                }
                //TODO
                function checkAndGetMessages(){
                    var phone = $('.chat-window').data('phone')
                    if (typeof phone !== "undefined" && phone !== '' ){
                        var lastOffset = $('.chat-window').data('lastMessageIndex')
                        $.ajax({
                          url: 'http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset,
                          error: function() {
                             alert('http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset);
                          },
                          dataType: 'json',
                          success: function(resp) {
                            $.each(resp.messages, function(index, message){
                                logicAddMessage(message)
                            })
                         },
                          type: 'GET'
                       });
                    }
                }
    
                function populateLastReceivedMessages(phone,messages){
                    var lastMessageCount = $('.chat-window').data('lastMessageIndex')
                    if (typeof lastMessageCount === "undefined"){
                        lastMessageCount = -1
                    }
                    var maxReceivedCount = 0
                    var minReceivedCount = 0
                    $.each(messages, function(index, message){
                                logicAddMessage(message)
                                if (maxReceivedCount < message.id){
                                    maxReceivedCount = message.id
                                }
                                if (minReceivedCount > message.id){
                                    minReceivedCount = message.id
                                }
                            })
                    if (maxReceivedCount > lastMessageCount){
                        $('.chat-window').data('lastMessageIndex',maxReceivedCount)
                    }
                    //fillGapMessages(phone,lastMessageCount,minReceivedCount)
                }
    
                function populateLastMessages(phone,count){
                    $.ajax({
                      url: 'http://'+host+'/getLastMessages/'+phone+'/'+count,
                      error: function() {
                         alert('http://'+host+'/getLastMessages/'+phone+'/'+count);
                      },
                      dataType: 'json',
                      success: function(resp) {
                        populateLastReceivedMessages(phone,resp.messages)
                      },
                      type: 'GET'
                   });
                }
    
                //TODO
                function openChatWindow(phone){
                    $('.chat-window').data('isChatOpen',true)
                    $('.chat-window').data('phone',phone)
                    populateLastMessages(phone,10)
                }
    
                //TODO progress here
                function launchChat(conversationTile){
                    var phone = conversationTile.data('conMeta').phone
                    if (lockConversation(phone)) {
                        if ($('.chat-window').data('isChatOpen')) {
                            closeCurrentChat()
                            openChatWindow(phone)
                        }else {
                            openChatWindow(phone)
                        }
                    } else {
                        alert('This conversation has already been taken!!')
                        fetchListAndPopulate()
                    }
                }
    
                function compareAndUpdateList(data){
                    $('#conversationList').empty()
                    $.each(data.conversations, function(index, value){
                                var template = $(conversationTileTemplate.replace('${phone}',value.phone));
                                $('#conversationList').append(template)
                                template.data('conMeta',value)
                            })
                    $('#conversationList').find('li').click(function(e){
                                    launchChat($(this))
                                })
                }
    
                function fetchListAndPopulate(){
                    $.ajax({
                          url: 'http://'+host+'/getUnreadConversations',
                          error: function() {
                             alert('http://'+host+'/getUnreadConversations');
                          },
                          dataType: 'json',
                          success: function(data) {
    
                            compareAndUpdateList(data)
                          },
                          type: 'GET'
                       })
                }
    
                function serverSendMessage(message){
                	var phone = $('.chat-window').data('phone');
                    $.ajax({
                          url: 'http://'+host+'/sendCCMessage/'+phone+'?message='+encodeURIComponent(message.message),
                          error: function() {
                             alert('http://'+host+'/sendCCMessage/'+phone+'?message='+encodeURIComponent(message.message));
                          },
                          dataType: 'json',
                          success: function(resp) {
                          	var lastMessageIndex = $('.chat-window').data('lastMessageIndex')
                          	fillGapMessages($('.chat-window').data('phone'),lastMessageIndex+1,resp)
                          	$('.chat-window').data('lastMessageIndex',resp)
                         },
                          type: 'GET'
                       });
                }
                
                function sendMessage(){
                    $('.chat-window').data('sendingMsgInProgress',true)
                    var phone = $('.chat-window').data('phone')
                    if (typeof phone !== "undefined" && phone !== '' ){
                        if ($('.chat-input').text() !== '') {
                            var message = {"inboundmsg" : false, "time":$.now(), "message" : $('.chat-input').text()}
                            serverSendMessage(message)
                            uiSendMessage()
                        }
                    }
                    $('.chat-window').data('sendingMsgInProgress',false)
                }
    
            function getAndDisplayNewMessages(){
                var phone = $('.chat-window').data('phone')
                if (typeof phone !== "undefined" && phone !== '' ){
                    if (!$('.chat-window').data('sendingMsgInProgress')){
                        var lastMessageIndex = $('.chat-window').data('lastMessageIndex')
                        if (typeof lastMessageIndex !== "undefined" && lastMessageIndex != -1){
                            var startOffset = lastMessageIndex + 1
                            var endOffset = startOffset + 100
                            $.ajax({
                              url: 'http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset,
                              error: function() {
                                 alert('http://'+host+'/getMessages/'+phone+'/'+startOffset+'/'+endOffset);
                              },
                              dataType: 'json',
                              success: function(resp) {
                                var maxIndexReceived = lastMessageIndex
                                $.each(resp.messages, function(index, message){
                                    logicAddMessage(message)
                                    if (maxIndexReceived < message.id){
                                        maxIndexReceived = message.id
                                    }
                                })
                                if (maxIndexReceived > lastMessageIndex) {
                                	$('.chat-window').data('lastMessageIndex',maxIndexReceived)
								}
                             },
                              type: 'GET'
                           });
                        }
                    }
                }
            }
    
            $(document).ready(function () {
                fetchListAndPopulate()
            })
    
            window.setInterval(function(){fetchListAndPopulate()}, 5000);
            window.setInterval(function(){getAndDisplayNewMessages()}, 2000);
})