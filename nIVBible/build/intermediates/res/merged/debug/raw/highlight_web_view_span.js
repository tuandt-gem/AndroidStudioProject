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

//function Ph_ChangeHighlightSentence(PhHighlightId, color, bSetColor) {
//    var elementPhSpans = document.getElementsByClassName(PhHighlightId);
//    if (elementPhSpans.length < 1)
//        return;
//
//    var elementPhSpan = elementPhSpans[0];
//    if (elementPhSpan != null && elementPhSpan.nodeType == 1) {//Element Node
//
//        elementPhSpan.style.borderBottom = "";
//        Ph_ChangeHighlightInElement(elementPhSpan, color, bSetColor);
////        if (bSetColor == 1)
////            elementPhSpan.style.backgroundColor = color;//backgroundColor
////        else
////            elementPhSpan.style.backgroundColor = 0;//backgroundColor
//    }
//}

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

        //1. For NIV&Amplified: <div..><p><sup>1</sup> text...</p><p>...
//        if (elementFromPos.childNodes.length == 1) {
//            var parentPnode = elementFromPos.parentNode;
//            while (parentPnode && parentPnode.nodeName.toLowerCase() != "p") {
//                parentPnode = parentPnode.parentNode;
//            }
//            if (parentPnode) {
//                elementFromPos = parentPnode;
//            }
//        }
//        if (elementFromPos.parentNode == null || elementFromPos.parentNode.parentNode == null || elementFromPos.parentNode.parentNode.nodeName.toLowerCase() == "body") {
//            Ph_VerseId = findRow3(elementFromPos);
//            Ph_HighlightNodeId = "Ph_chapter_verse_" + Ph_VerseId;
//
//            var span = document.createElement("span");
//            span.innerHTML = elementFromPos.innerHTML;
//            span.setAttribute("class", Ph_HighlightNodeId);
//            span.style.borderBottom = "2px dashed #FF0000";
//            elementFromPos.innerHTML = span.outerHTML;
//            return Ph_HighlightNodeId;
//        }

        //2. There are serveral "<p xmlns="http://www.w3.org/1999/xhtml" class="sb">" of Chapter(called. chapterChildNode).
        //nodeFromPos is TextNode and it has many super node. ex: "...Consequently I entreat <span class="smcap">YOU</span> by the...", "YOU" has super "span" node.
        //So find the cild node of chapterChildNode from nodeFromPos.(called. grandChildNodeOfChpater)
        //And highlight from the verse to next verse. ex: from "<a id="chapter12_verse1"></a>" to "<a id="chapter12_verse1"></a>"
        var grandChildNodeOfChpater = nodeFromPos;
        while (grandChildNodeOfChpater.parentNode) {
            var tParentNode = grandChildNodeOfChpater.parentNode;
            if (tParentNode.nodeName.toLowerCase() == "span" && tParentNode.id) {//Android version, <span id="chapter1_verse3" /> <b>...</b> ...
                var re = /(chapter)(\\d+)(_verse)(\\d+)/;
                var nameList = tParentNode.id.split(re);
                if (nameList.length == 6) {//"", "chapter", digits, "_verse", digits, ""
                    Ph_ChapterIndex = nameList[2];
                    Ph_HighlightNodeId = "Ph_" + tParentNode.id;
                    break;
                }
            }
            if (tParentNode.nodeName.toLowerCase() == "body")//2 Samuel 1page's verse1, verse2
                break;
            if (tParentNode.nodeName.toLowerCase() == "p") {
                var attr = tParentNode.getAttribute("class");
                if (attr && attr.length > 0/* == "sb"*/) //Element Node
                    break;
            }
            grandChildNodeOfChpater = tParentNode;// grandChildNodeOfChpater.parentNode;
        }

        if (grandChildNodeOfChpater == null)
            return null;

        //Create new span
        var span = document.createElement("span");

        var currentVerse = grandChildNodeOfChpater.previousSibling;
        while (currentVerse != null) {
            if (Ph_HighlightNodeId == null && currentVerse.nodeName.toLowerCase() == "span" && currentVerse.id) {
                var re = /(chapter)(\\d+)(_verse)(\\d+)/;
                var nameList = currentVerse.id.split(re);
                if (nameList.length == 6) {//"", "chapter", digits, "_verse", digits, ""
                    Ph_ChapterIndex = nameList[2];
                    Ph_HighlightNodeId = "Ph_" + currentVerse.id;
                    break;
                }
            }

            var oldCurrentVerse = currentVerse;
            currentVerse = currentVerse.previousSibling;
            span.insertBefore(oldCurrentVerse, span.firstChild);
        }

        if (Ph_HighlightNodeId == null)
            Ph_HighlightNodeId = "Ph_Verse" + (Ph_VerseId++);//makeid();

        var copynode = grandChildNodeOfChpater.cloneNode();
        copynode.innerHTML = grandChildNodeOfChpater.innerHTML;
        span.appendChild(copynode);

        var nextVerse = grandChildNodeOfChpater.nextSibling;
        while (nextVerse != null) {
            var nextANode = nextVerse;
            if (nextVerse.nodeType == 1 && nextVerse.nodeName.toLowerCase() == "p") {
                var attr = nextVerse.getAttribute("class");
                if (attr && attr.length > 0/* == "sb"*/)
                    nextANode = nextVerse.firstChild;
            } 

            if (nextANode.nodeName.toLowerCase() == "span" && nextANode.id) {
                var re = /(chapter)(\\d+)(_verse)(\\d+)/;
                var nameList = nextANode.id.split(re);
                if (nameList.length == 6) {
                    break;
                }
            }
            var oldNextVerse = nextVerse;
            nextVerse = nextVerse.nextSibling;
            span.appendChild(oldNextVerse);
        }

        span.setAttribute("class", Ph_HighlightNodeId);
        span.style.borderBottom = "2px dashed #FF0000";
        //span.style.textDecoration = "underline";
        // span.style.textDecorationStyle = "dashed";

        grandChildNodeOfChpater.parentNode.replaceChild(span, grandChildNodeOfChpater);
    }

    if (Ph_HighlightNodeId) {
        showAndroidContextMenu(Ph_HighlightNodeId);
        return Ph_HighlightNodeId;
    }
    return "";
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
	var startNumber = PhHighlightNodeId.indexOf("Ph_");
    if (startNumber !== 0)
        return;
    PhHighlightNodeId = PhHighlightNodeId.substr(3);

    var elementFromPos = document.getElementById(PhHighlightNodeId);
    if (elementFromPos == null || elementFromPos.nodeName.toLowerCase() != "span")
        return;
    
    var elementParent = null;
    var nextVerse = elementFromPos.nextSibling;
    if (elementFromPos.id) {//Android
        var re = /(chapter)(\\d+)(_verse)(\\d+)/;
        var nameList = elementFromPos.id.split(re);
        if (nameList.length == 6) {
            elementParent = elementFromPos;
            nextVerse = elementFromPos.firstChild;
        }
    }
    
    if (elementParent == null) {
    	elementParent = elementFromPos.parentElement;
    	if (elementParent.nodeName.toLowerCase() != "p")
        	return;
    }

    //Remove element
    while (nextVerse != null) {
        if (nextVerse.nodeName.toLowerCase() == "span" && nextVerse.id) {
            var re = /(chapter)(\\d+)(_verse)(\\d+)/;
            var nameList = nextVerse.id.split(re);
            if (nameList.length == 6) {
                break;
            }
        }
        var oldNextVerse = nextVerse;
        nextVerse = nextVerse.nextSibling;
        elementParent.removeChild(oldNextVerse);
    }
    
    var fragment = Ph_createElement(highlightHtml);
    elementParent.insertBefore(fragment, nextVerse);//elementFromPos.nextSibling);
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
