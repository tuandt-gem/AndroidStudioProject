//1 	ELEMENT_NODE
//2 	ATTRIBUTE_NODE
//3 	TEXT_NODE
//4 	CDATA_SECTION_NODE
//5 	ENTITY_REFERENCE_NODE
//6 	ENTITY_NODE
//7 	PROCESSING_INSTRUCTION_NODE
//8 	COMMENT_NODE
//9 	DOCUMENT_NODE
//10 	DOCUMENT_TYPE_NODE
//11 	DOCUMENT_FRAGMENT_NODE
//12 	NOTATION_NODE

// We're using a global variable to store the number of occurrences
var Ph_HighlightNodeId = null;
var Ph_ChapterIndex = "0";
var Ph_VerseId = "1";

function showAndroidContextMenu(highlightId) {
    Android.showContextMenu(highlightId);
}
function setHighlightedSentence(sentence) {
    Android.setHighlightedSentence(sentence);
}
function saveHtmlTo() {
    Android.saveHtmlTo(document.lastChild.outerHTML);
}

// helper function, recursively searches in elements and their child nodes
function getNodeFromPoint(x, y){
    if(y===undefined){
        y = x.y;
        x = x.x;
    }
    var el = document.elementFromPoint(x, y);
    if (el == null)
    	return null;

    var nodes = el.childNodes;
    for ( var i = 0, n; n = nodes[i++];) {
        if (n.nodeType === 3) {
            var r = document.createRange();
            r.selectNode(n);
            var rects = r.getClientRects();
            for ( var j = 0, rect; rect = rects[j++];) {
                if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {
                    return n;
                }
            }
        }
    }
    return el;
};

function Ph_CheckHighlightSentence(element) {
    var checked_highlight = null;
    while (element != null) {
        // if (element.nodeName.toLowerCase() == 'span') {
            var classAttr = element.getAttribute("class");
            if (classAttr != null && classAttr.indexOf("Ph_chapter") === 0) {//classAttr.startsWith("Ph_chapter") not working on safari
                checked_highlight = element;
                break;
            }
        // }
        element = element.parentElement;
    }
    return checked_highlight;
}

function Ph_ChangeHighlightElement(element, color, PhHighlightId) {
    for (var i = element.childNodes.length - 1; i >= 0; i--) {
        var subnode = element.childNodes[i];
        if (subnode.nodeType == 3) {//text node
            var span = document.createElement("span");
            span.appendChild(document.createTextNode(subnode.nodeValue));
            span.setAttribute("class", "sub_" + PhHighlightId);
            span.style.backgroundColor = color;
            element.replaceChild(span, subnode);
        } else if (subnode.nodeType == 1) {//element node
            Ph_ChangeHighlightElement(subnode, color, PhHighlightId);
        }
    };
}

function Ph_createElement(htmlStr) {
    var frag = document.createDocumentFragment(),
        temp = document.createElement('div');
    temp.innerHTML = htmlStr;
    while (temp.firstChild) {
        frag.appendChild(temp.firstChild);
    }
    return frag;
}

function Ph_SetHighlightSentence(PhHighlightNodeId, color, highlightHtml) {
    var startNumber = PhHighlightNodeId.indexOf("Ph_chapter_verse_");
    if (startNumber !== 0)
        return;
    
    var verseId = PhHighlightNodeId.substr(17);
    var ptags = document.getElementsByTagName('p');
    var targetTag = null;
    for (var i = 0; i < ptags.length; ++i) {
        var re = /(<sup.*)(\\d+)(.*<\\/sup>)/;
        var nameList = ptags[i].innerHTML.split(re);
        if (nameList[2] == verseId) {
            //if (spans[i].innerText === stringToMatch) {
            // found it ...
            targetTag = ptags[i];
            break;
        }
    }
    if (targetTag) {
        targetTag.innerHTML = highlightHtml;
    }
    
    //var fragment = Ph_createElement(highlightHtml);
    //elementParent.insertBefore(fragment, elementFromPos.nextSibling);
}

function Ph_ChangeHighlightSentence(PhHighlightId, color, bSetColor) {
    var elementPhSpans = document.getElementsByClassName(PhHighlightId);
    if (elementPhSpans.length < 1)
        return "";
    
    var elementPhSpan = elementPhSpans[0];
    if (elementPhSpan != null && elementPhSpan.nodeType == 1) {//Element Node
        // elementPhSpan.style.textDecoration = "none";
        elementPhSpan.style.borderBottom = "";
        
        if (bSetColor == -1) {
        	setHighlightedSentence(elementPhSpan.outerHTML);
            return elementPhSpan.outerHTML;
        }
        
        elementPhSpan.style.backgroundColor = 'transparent';
        var phHighlightSubNodes = elementPhSpan.getElementsByClassName("sub_" + PhHighlightId);
        if (phHighlightSubNodes.length > 0) {
            for (var i = phHighlightSubNodes.length - 1; i >= 0; i--) {
                if (bSetColor == 1)
                    phHighlightSubNodes[i].style.backgroundColor = color;
                else
                    phHighlightSubNodes[i].style.backgroundColor = 'transparent';
            };
        } else if (bSetColor == 1) {
            Ph_ChangeHighlightElement(elementPhSpan, color, PhHighlightId);
        }
        
    	setHighlightedSentence(elementPhSpan.outerHTML);
        return elementPhSpan.outerHTML;
    }
    
    return "";
}

function findRow3(node)
{
    var i = 1;
    while (node = node.previousSibling) {
        if (node.nodeType === 1) { ++i }
    }
    return i;
}

function Ph_FindHighlightSentence(x,y,width,height) {
	x = parseInt(x * (window.innerWidth / width));
    y = parseInt(y * (window.innerHeight / height));
	
    var nodeFromPos = getNodeFromPoint(x,y);
    var elementFromPos = document.elementFromPoint(x,y);
    if (elementFromPos == null)
        return null;

    Ph_HighlightNodeId = null;
    
    if (nodeFromPos.nodeType == 3) {//Text node
        var checked_PhSpan = Ph_CheckHighlightSentence(elementFromPos);
        if (checked_PhSpan != null) {
            Ph_HighlightNodeId = checked_PhSpan.getAttribute("class");
            // checked_PhSpan.style.textDecoration = "underline";
            // checked_PhSpan.style.textDecorationStyle = "dashed";
            checked_PhSpan.style.borderBottom = "2px dashed #FF0000";
            
            showAndroidContextMenu(Ph_HighlightNodeId);
            return Ph_HighlightNodeId;
        }

        var containerPNode= elementFromPos;
        while (containerPNode) {
            if (containerPNode.nodeName.toLowerCase() == "body")//2 Samuel 1page's verse1, verse2
                return "";
            if (containerPNode.nodeName.toLowerCase() == "p") {
            	var re = /(<sup.*)(\\d+)(.*<\\/sup>)/;
                var nameList = containerPNode.innerHTML.split(re);
                
                if (nameList.length == 5) {
                	Ph_HighlightNodeId = "Ph_chapter_verse_" + nameList[2];
        		}
            	break;
            }
            containerPNode = containerPNode.parentNode;
        }

        if (Ph_HighlightNodeId == null) {
            return "";
        	//Ph_HighlightNodeId = "Ph_Verse" + (Ph_VerseId++);//makeid();
        }

		//containerPNode.setAttribute("class", Ph_HighlightNodeId);
        //containerPNode.style.borderBottom = "2px dashed #FF0000";

        //Create new span
        var span = document.createElement("span");
        span.innerHTML = containerPNode.innerHTML;
//      var copynode = containerPNode.cloneNode();
//      copynode.innerHTML = containerPNode.innerHTML;
//      span.appendChild(copynode);

        span.setAttribute("class", Ph_HighlightNodeId);
        span.style.borderBottom = "2px dashed #FF0000";

        containerPNode.innerHTML = span.outerHTML;
        //containerPNode.parentNode.replaceChild(span, containerPNode);
    }

    if (Ph_HighlightNodeId) {
        showAndroidContextMenu(Ph_HighlightNodeId);
        return Ph_HighlightNodeId;
    }
    return "";
}

var Ph_HighlightNode = null;
function Ph_HighlightSentence1(x,y,color) {
    var nodeFromPos = getNodeFromPoint(x,y);
    var element = document.elementFromPoint(x,y);
    if (element.style.display == "none" || element.nodeName.toLowerCase() == 'select')
        return null;

    Ph_HighlightNode = null;

    if (nodeFromPos.nodeType == 3) {//Text node
        var highlightNode = null;
        var classAttributes = "";
        if (element.childNodes.length == 1 && element.firstChild == nodeFromPos) {
            highlightNode = element;
            classAttributes = element.getAttribute("class");
        } else {
            var value = nodeFromPos.nodeValue;
            var span = document.createElement("span");
            span.textContent = value;
            span.setAttribute("class","PhSentenceHighlight");
            span.style.backgroundColor=color;
            span.style.color="black";
            highlightNode = span;
        }


        if (highlightNode == element) {
            highlightNode.style.backgroundColor=color;
        } else {
            element.replaceChild(highlightNode, nodeFromPos);
        }

        Ph_HighlightNode = highlightNode;
    }

    return Ph_HighlightNode;
}

function onTest_highlight(e) {
	Ph_FindHighlightSentence(e.clientX, e.clientY);
}
