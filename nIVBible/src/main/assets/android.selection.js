// Namespace
var android = {};
android.selection = {};

android.selection.selectionStartRange = null;
android.selection.selectionEndRange = null;

/** The last point touched by the user. { 'x': xPoint, 'y': yPoint } */
android.selection.lastTouchPoint = null;

/**
 * Starts the touch and saves the given x and y coordinates as last touch point
 */
android.selection.startTouch = function (x, y) {
    window.TextSelection.jsLog("android.selection.startTouch ");
    android.selection.lastTouchPoint = {
        'x': x,
        'y': y
    };
};

/**
 * Checks to see if there is a selection.
 *
 * @return boolean
 */
android.selection.hasSelection = function () {
    return window.getSelection().toString().length > 0;
};

/**
 * Clears the current selection.
 */
android.selection.clearSelection = function () {
    window.TextSelection.jsLog("android.selection.clearSelection ");
    try {
        currentColor = "initial";

//      if current selection clear it.
        if (window.getSelection) {
            if (window.getSelection().empty) {
                // Chrome
                window.getSelection().empty();
            } else if (window.getSelection().removeAllRanges) {
                // Firefox
                window.getSelection().removeAllRanges();
            }
        } else if (document.selection) {
            // IE?
            document.selection.empty();
        }
//        window.getSelection().removeAllRanges();
    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

/**
 * Handles the single touch action
 */
function singleTouchWord() {
    try {
        var range = document.caretRangeFromPoint(android.selection.lastTouchPoint.x, android.selection.lastTouchPoint.y);

        range.expand("word");

        var preSelectionRange = range.cloneRange();
        preSelectionRange.selectNodeContents(document.getElementsByTagName('body')[0]);
        preSelectionRange.setEnd(range.startContainer, range.startOffset);

//    Get start/end position
        var start = preSelectionRange.toString().length;
        var end = start + range.toString().length;


        window.TextSelection.onSingleTouchWord(start, end);
    } catch (e) {
        window.TextSelection.jsError("Single touch error\n" + e);
    }
}

/**
 * Handles the long touch action by selecting the last touched element.
 */
android.selection.longTouch = function () {
    window.TextSelection.jsLog("longTouch ");
    try {
        android.selection.clearSelection();
        var sel = window.getSelection();
        var range = document.caretRangeFromPoint(
            android.selection.lastTouchPoint.x,
            android.selection.lastTouchPoint.y);

        range.expand("word");
        var text = range.toString();
        if (text.length == 1) {
            var baseKind = jpntext.kind(text);
            if (baseKind != jpntext.KIND['ascii']) {
                try {
                    do {
                        range.setEnd(range.endContainer, range.endOffset + 1);
                        text = range.toString();
                        var kind = jpntext.kind(text);
                    } while (baseKind == kind);
                    range.setEnd(range.endContainer, range.endOffset - 1);
                } catch (e) {
                }
                try {
                    do {
                        range.setStart(range.startContainer,
                            range.startOffset - 1);
                        text = range.toString();
                        var kind = jpntext.kind(text);
                    } while (baseKind == kind);
                    range.setStart(range.startContainer, range.startOffset + 1);
                } catch (e) {
                }
            }
        }

        if (text.length > 0) {
            sel.addRange(range);

            var temp = sel.getRangeAt(0);
            window.TextSelection.jsLog("android.selection.longTouch " + temp);

            android.selection.saveSelectionStart();
            android.selection.saveSelectionEnd();

            window.TextSelection.startSelectionMode();
            android.selection.selectionChanged(true);
        }
    } catch (err) {
        window.TextSelection.jsError("android.selection.longTouch + " + err);
    }
};

/**
 * Tells the app to show the context menu.
 */
android.selection.selectionChanged = function (isReallyChanged) {
    window.TextSelection.jsLog("android.selection.selectionChanged ");

    try {
        var sel = window.getSelection();
        if (!sel) {
            return;
        }

        snapSelectionToWord();
        var range = sel.getRangeAt(0);

//         Add spans to the selection to get page offsets
        var selectionStart = $("<span id=\"selectionStart\">&#xfeff;</span>");
        var selectionEnd = $("<span id=\"selectionEnd\"></span>");

        var startRange = document.createRange();
        startRange.setStart(range.startContainer, range.startOffset);
        startRange.insertNode(selectionStart[0]);

        var endRange = document.createRange();
        endRange.setStart(range.endContainer, range.endOffset);
        endRange.insertNode(selectionEnd[0]);

        var left = selectionStart.offset().left;
        var top = selectionStart.offset().top + selectionStart.height();
        var right = selectionEnd.offset().left;
        var bottom = selectionEnd.offset().top + selectionEnd.height();

        var handleBounds = "{'left': " + (selectionStart.offset().left) + ", ";
        handleBounds += "'top': "
            + (selectionStart.offset().top + selectionStart.height())
            + ", ";
        handleBounds += "'right': " + (selectionEnd.offset().left) + ", ";
        handleBounds += "'bottom': "
            + (selectionEnd.offset().top + selectionEnd.height()) + "}";

//        window.TextSelection.jsError("@@@@ ===== range count " + sel.rangeCount);
//        window.TextSelection.jsError("@@@@ left=" + left);
//        window.TextSelection.jsError("@@@@ top=" + top);
//        window.TextSelection.jsError("@@@@ bottom=" + bottom);
//        window.TextSelection.jsError("@@@@ right=" + right);

//         Pull the spans
        selectionStart.remove();
        selectionEnd.remove();

//         Reset range
        sel.removeAllRanges();
        sel.addRange(range);

//         Rangy
        var rangyRange = android.selection.getRange();

//         This use rangy-textrange.js but not works
//        rangy.getSelection().expand("word");

//         Text to send to the selection
        var text = window.getSelection().toString();

//         Set the content width
        window.TextSelection.setContentWidth(document.body.clientWidth);

//        var savedSel = rangy.saveSelection();
//        window.TextSelection.jsError("@@@ savedString=" + savedSel.toString());
//        var savedString = JSON.stringify(savedSel);
//        var savedString = objToString(savedSel);
//        window.TextSelection.jsError("@@@ savedString=" + savedString);

//        for (var key in savedSel) {
//            if (Object.prototype.hasOwnProperty.call(savedSel, key)) {
//                var val = savedSel[key];
//                window.TextSelection.jsError("@@@ log " + key + "=" + val);
//            }
//        }

//         Save selection and send to native
        var saveSel = doSave();

//         Get fontSize
        var res = getSelectionColorAndFontSize();
        var fontSize = 0;

        if (res !== undefined) {
            fontSize = parseInt(res.fontSize, 10);
        }

//        alert(findRowIndex(range.startContainer) + "-" + findRowIndex(range.endContainer));
        var startPara = getParagraphOfNode(range.startContainer);
        var endPara = getParagraphOfNode(range.endContainer);

        var startClass = startPara.className;
        var endClass = endPara.className;

        var startVerseNumber = findRowIndex(startPara);
        var endVerseNumber = findRowIndex(endPara);

        window.TextSelection.saveSelection(saveSel.start, saveSel.end,
            startVerseNumber, endVerseNumber,
            range.toString(), fontSize);

//         Tell the interface that the selection changed
        window.TextSelection.selectionChanged(rangyRange, text, fontSize, handleBounds,
            isReallyChanged);
    } catch (e) {
        window.TextSelection.jsError("selectionChanged " + e);
    }
};

/**
* Index of row in parent
*/
function findRowIndex(node) {
    var paras = document.getElementsByTagName("p");
//    var noneVerse = 0;
//    var index = 1;
    var index = 0;

    for (var i = 0, len = paras.length; i < len; i++) {
        var para = paras[i];
//        if (para.className) {
//            noneVerse++;
//        } else{
            if (para.innerHTML == node.innerHTML) {
//                index = i - noneVerse + 1;
                index = i;
                break;
            }
//        }
    }

//    return index < 1 ? 1  : index;
    return index;
}

/**
*   Paragraph of selection
*/
function getParagraphOfNode(node) {
    var parent = node;
//    alert(parent.parentNode.localName);
    while (parent != null && parent.localName !== "p") {
        parent = parent.parentNode;
    }

    return parent;
}

/**
*   Scroll to verse with number
**/
function scrollToVerseNumber(verseNumber, highlightAfterScroll) {
// Verse number start with 1
//    verseNumber--;

    window.TextSelection.jsError("scrollToVerseNumber " + verseNumber);
    window.TextSelection.jsError("highlightAfterScroll " + highlightAfterScroll);
    var paras = document.getElementsByTagName("p");
    var para = paras[verseNumber];

//    window.TextSelection.jsError("scrollToVerseNumber para=" + para.innerHTML);
    $('html, body').animate({
//      scrollTop: target.offset().top
        scrollTop: (para.offsetTop - 50 + para.offsetHeight / 2)
    },
    1000);

    if (highlightAfterScroll) {
        highlightTemp(para);
    }
//var index = 1;
//      var noneVerse = 0;
//    for (var i = 0, len = paras.length; i < len; i++) {
//        var para = paras[i];
//        if (para.className) {
//            noneVerse++;
//        } else{
//            if (verseNumber + noneVerse == i) {
////                    Scroll to this
//                window.TextSelection.jsError("scrollToVerseNumber i=" + i);
//                window.TextSelection.jsError("scrollToVerseNumber para=" + para.innerHTML);
//                window.TextSelection.jsError("scrollToVerseNumber noneVerse=" + noneVerse);
//                $('html, body').animate({
////                        scrollTop: target.offset().top
//                    scrollTop: (para.offsetTop - 50 + para.offsetHeight / 2)
//                },
//                1000);
//
//                highlightTemp(para);
//            }
//
////                if (para.innerHTML == node.innerHTML) {
////                    index = i - noneVerse + 1;
////                    break;
////                }
//        }
//    }
}

function highlightTemp(node) {
    node.style.backgroundColor = "#D3D3D3";
    setTimeout(function(){ window.TextSelection.jsLog("para " + node.innerHTML); node.style.backgroundColor = "initial";}, 2000);
}

android.selection.getRange = function () {
    var serializedRangeSelected = rangy.serializeSelection();
    var serializerModule = rangy.modules.Serializer;
    if (serializedRangeSelected != '') {
        if (rangy.supported && serializerModule && serializerModule.supported) {
            var beginingCurly = serializedRangeSelected.indexOf("{");
            serializedRangeSelected = serializedRangeSelected.substring(0,
                beginingCurly);
            return serializedRangeSelected;
        }
    }
}

/**
 * Returns the last touch point as a readable string.
 */
android.selection.lastTouchPointString = function () {
    if (android.selection.lastTouchPoint == null)
        return "undefined";
    return "{" + android.selection.lastTouchPoint.x + ","
        + android.selection.lastTouchPoint.y + "}";
};

android.selection.saveSelectionStart = function () {
    try {
//         Save the starting point of the selection
        var sel = window.getSelection();
        var range = sel.getRangeAt(0);
        var saveRange = document.createRange();
        saveRange.setStart(range.startContainer, range.startOffset);
        android.selection.selectionStartRange = saveRange;

    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

android.selection.saveSelectionEnd = function () {
    try {
//         Save the end point of the selection
        var sel = window.getSelection();
        var range = sel.getRangeAt(0);
        var saveRange = document.createRange();
        saveRange.setStart(range.endContainer, range.endOffset);
        android.selection.selectionEndRange = saveRange;

    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

/**
 * Sets the last caret position for the start handle.
 */
android.selection.setStartPos = function (x, y) {
    try {
//        var range = document.caretRangeFromPoint(x, y);
//        range.expand("word");
//
//        window.TextSelection.jsError("setStartPos " + range.startContainer);
//        var preSelectionRange = range.cloneRange();
//        preSelectionRange.selectNodeContents(document.getElementsByTagName('body')[0]);
//        preSelectionRange.setStart(range.startContainer, range.startOffset);
//        preSelectionRange.setEnd(range.endContainer, range.endOffset);

        android.selection.selectBetweenHandles(document.caretRangeFromPoint(x, y), android.selection.selectionEndRange);
    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

/**
 * Sets the last caret position for the end handle.
 */
android.selection.setEndPos = function (x, y) {
    try {
//        var range = document.caretRangeFromPoint(x, y);
//                range.expand("word");
//
//        window.TextSelection.jsError("setEndPos " + range.startContainer);
//        var preSelectionRange = range.cloneRange();
//        preSelectionRange.selectNodeContents(document.getElementsByTagName('body')[0]);
//        preSelectionRange.setStart(range.startContainer, range.startOffset);
//        preSelectionRange.setEnd(range.endContainer, range.endOffset);

        android.selection.selectBetweenHandles(android.selection.selectionStartRange, document.caretRangeFromPoint(x, y));
    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

android.selection.restoreStartEndPos = function () {
    try {
       android.selection.selectBetweenHandles(android.selection.selectionEndRange, android.selection.selectionStartRange);
    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

/**
 * Selects all content between the two handles
 */
android.selection.selectBetweenHandles = function (startCaret, endCaret) {
//    window.TextSelection.jsLog("@@@@ android.selection.selectBetweenHandles>>> " + android.selection.selectionStartRange.startContainer);
//    window.TextSelection.jsLog("@@@@ android.selection.selectBetweenHandles>>> " + android.selection.selectionStartRange.startOffset);

    try {
        if (startCaret && endCaret) {
            var rightOrder = startCaret.compareBoundaryPoints(
                    Range.START_TO_END, endCaret) <= 0;

            if (rightOrder) {
                window.TextSelection.jsError("rightOrder ");
                android.selection.selectionStartRange = startCaret;
                android.selection.selectionEndRange = endCaret;
            } else {
                window.TextSelection.jsError("NOT rightOrder ");
                startCaret = android.selection.selectionStartRange;
                endCaret = android.selection.selectionEndRange;
            }
            var range = document.createRange();
            range.setStart(startCaret.startContainer, startCaret.startOffset);
            range.setEnd(endCaret.startContainer, endCaret.startOffset);
            android.selection.clearSelection();
            var selection = window.getSelection();
            selection.addRange(range);
            android.selection.selectionChanged(rightOrder);
        } else {
            android.selection.selectionStartRange = startCaret;
            android.selection.selectionEndRange = endCaret;
        }

//        window.TextSelection.jsLog("@@@@ android.selection.selectBetweenHandles<<< " + android.selection.selectionStartRange.startContainer);
//        window.TextSelection.jsLog("@@@@ android.selection.selectBetweenHandles<<< " + android.selection.selectionStartRange.startOffset);
    } catch (e) {
        window.TextSelection.jsError(e);
    }
};

var savedRange;
function endDrag() {
    var sel = window.getSelection();
    window.TextSelection.jsLog("endDrag " + sel.getRangeAt(0).toString());
//    highlight("initial");
}

function onDragDone(color) {
    currentColor = color;
    var sel = window.getSelection();
    window.TextSelection.jsLog("onDragDone " + sel.getRangeAt(0).toString());
    window.TextSelection.jsLog("onDragDone " + currentColor);
//    highlight(currentColor);
    window.TextSelection.saveHtml(document.getElementsByTagName("html")[0].innerHTML);
}

var currentColor;
/**
 * Highlight with color
 */
function makeEditableAndHighlight(colour) {
    currentColor = colour;
    var range, sel = window.getSelection();

    window.TextSelection.jsLog(":::makeEditableAndHighlight::: " + sel.rangeCount);

    if (sel.rangeCount && sel.getRangeAt) {
        range = sel.getRangeAt(0);
    }
    document.designMode = "on";
    if (range) {
//        sel.removeAllRanges();
        sel.addRange(range);
    }
//     Use HiliteColor since some browsers apply BackColor to the whole block
    if (!document.execCommand("HiliteColor", false, colour)) {
        document.execCommand("BackColor", false, colour);
    }
    document.designMode = "off";
}

/**
* Restore selection by start-end position, mark color and add note icon
*/
function restoreSavedSelection(start, end, color, noteId) {
    var savedSel = new Object();
    savedSel.start = start;
    savedSel.end = end;
    savedSel.color = color;
    restoreSelection(savedSel, noteId);
    android.selection.clearSelection();
}

/**
*   Create selection range from saved values
*/
function createSelectionRange(savedSel) {
    var containerEl = document.getElementsByTagName('body')[0];
    var charIndex = 0, range = document.createRange();
    range.setStart(containerEl, 0);
    range.collapse(true);
    var nodeStack = [containerEl], node, foundStart = false, stop = false;

    while (!stop && (node = nodeStack.pop())) {
        if (node.nodeType == 3) {
            var nextCharIndex = charIndex + node.length;
            if (!foundStart && savedSel.start >= charIndex
                && savedSel.start <= nextCharIndex) {
                range.setStart(node, savedSel.start - charIndex);
                foundStart = true;
            }
            if (foundStart && savedSel.end >= charIndex
                && savedSel.end <= nextCharIndex) {
                range.setEnd(node, savedSel.end - charIndex);
                stop = true;
            }
            charIndex = nextCharIndex;
        } else {
            var i = node.childNodes.length;
            while (i--) {
                nodeStack.push(node.childNodes[i]);
            }
        }

    }

    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);

    return range;
}
/**
* Restore selection and it's note
*/
function restoreSelection(savedSel, noteId) {
// Create range selection and add note icon
    var range = createSelectionRange(savedSel);

    if (noteId != 0) {
// Remove note icon if pass initial color
        if (!savedSel.color || savedSel.color === "initial") {
            removeNoteElement(noteId)
        } else {
            insertNoteIcon(noteId);
        }
    } else {
        window.TextSelection.jsLog("No note in selection");
    }

// Create range selection and remove underline first
//    android.selection.clearSelection();
//    range = createSelectionRange(savedSel);
//    removeUnderlineSelection();
//    highlight("initial");

// Create range selection and highlight or underline
//    android.selection.clearSelection();
    range = createSelectionRange(savedSel);
    if (savedSel.color == "underline") {
        underlineSelection();
    } else {

        highlight(savedSel.color);
    }

//    } catch (e) {
//        window.TextSelection.jsError("restoreSelection\n" + e);
//    }
}

/**
* Select a selection
*/
//function selectSelectionRange(start, end, color, noteId) {
function selectSelectionRange(start, end) {
    var savedSel = new Object();
    savedSel.start = start;
    savedSel.end = end;

    var range = createSelectionRange(savedSel);
//         Start selection mode
//        android.selection.saveSelectionStart();
//        android.selection.saveSelectionEnd();
//        android.selection.selectionChanged(false);

    notifyRangeCoords();
}

/**
* Notify to native code
*/
function notifyRangeCoords() {
    var coords = getSelectionCoords();
    window.TextSelection.jsLog("logRangeCoords " + coords.left + "-" + coords.top + "-" + coords.bottom + "-" + coords.right);

    var sel = getSelectionColorAndFontSize();
    var fontSize = 0;
    if (sel) {
        fontSize = sel.fontSize;
        window.TextSelection.jsLog("logRangeCoords font " + fontSize);
    }

    window.TextSelection.notifyRangeCoords(parseInt(coords.left, 10),
        parseInt(coords.top, 10),
        parseInt(coords.bottom, 10),
        parseInt(coords.right, 10),
        parseInt(fontSize, 10));
}

/**
* Get current selection coordinates in webview
*/
function getSelectionCoords(win) {

    win = win || window;
    var doc = win.document;
    var sel = doc.selection, range, rects, rect;
    var left = 0, top = 0, bottom = 0, right = 0;

    if (sel) {
        if (sel.type != "Control") {
            range = sel.createRange();
            range.collapse(true);
            left = range.boundingLeft;
            top = range.boundingTop;
            bottom = range.boundingBottom;
            right = range.boundingRight;
        }
    } else if (win.getSelection) {
        sel = win.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0).cloneRange();
            if (range.getClientRects) {
                range.collapse(true);
                rects = range.getClientRects();
                if (rects.length > 0) {
                    rect = rects[0];
                }
                left = rect.left;
                top = rect.top;
                bottom = rect.bottom;
                right = rect.right;

            }
            // Fall back to inserting a temporary element
            if (left == 0 && top == 0 && bottom == 0 && right ==0) {
                var span = doc.createElement("span");
                if (span.getClientRects) {
                    // Ensure span has dimensions and position by
                    // adding a zero-width space character
                    span.appendChild( doc.createTextNode("\u200b") );
                    range.insertNode(span);
                    rect = span.getClientRects()[0];
                    left = rect.left;
                    top = rect.top;
                    bottom = rect.bottom;
                    right = rect.right;

                    var spanParent = span.parentNode;
                    spanParent.removeChild(span);

                    // Glue any broken text nodes back together
                    spanParent.normalize();
                }
            }
        }
    }

    return { left: left, top: top, bottom: bottom, right: right };
}


function underlineSelection() {
    setSelectionTextDecoration("underline", true);
}

/**
*   Remove underline of current selection
*/
function removeUnderlineSelection() {

    var sel = window.getSelection();
    var range = sel.getRangeAt(0);

    var nodes = getRangeTextNodes(range);

    for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
//        unhighlight(node);
        var parent = node.parentNode;
//        window.TextSelection.jsLog("====\n!@@@@@@@@@@! span= " + parent.getElementsByTagName("span").length);
        if (parent.nodeName != "SPAN") {
            unhighlight(node);
//            window.TextSelection.jsLog("1111");
        } else {
//            window.TextSelection.jsLog("2222");
            unhighlight(parent);
        }

//        window.TextSelection.jsError("!@@@@@@@@@@! = " + parent.nodeType);
//        window.TextSelection.jsError("!@@@@@@@@@@! = " + parent.textContent);
//        window.TextSelection.jsError("!@@@@@@@@@@! = " + parent.nodeName);
//        continue;
//
//        var r = document.createRange();
//        r.setStart(node, 0);
//        r.setEnd(node, node.textContent.length);
//        setRangeTextDecoration(r, "none");
//
//
////        r.setStart(node, )
//        if (node.style) {
//            node.style.textDecoration = "none";
//        }
    }
}

function removeHighlightAndUnderline(start, end) {
//    var range = document.getSelection().getRangeAt(0);

    var savedSel = new Object();
    savedSel.start = start;
    savedSel.end = end;


    var range = createSelectionRange(savedSel);
    removeUnderlineSelection();

//    range = createSelectionRange(savedSel);
//    highlight("initial");
}

/**
*   Set range textDecoration
*/
function setRangeTextDecoration(range, textDecoration) {
    var rangeText = range.extractContents();
    var span= document.createElement("span");
    span.style.textDecoration = textDecoration;
    span.appendChild(rangeText);
    range.insertNode(span);
}

/**
*   Underline current selection
*/
function setSelectionTextDecoration(textDecoration, onlyTextNode) {
    var sel = window.getSelection();
    var range = sel.getRangeAt(0);
    var startOffset = range.startOffset;
    var endOffset = range.endOffset;
//

    var nodes
    if (onlyTextNode == true) {
        nodes = getRangeTextNodes(range);
    } else {
        nodes = getRangeNodes(range);
    }

    var len = nodes.length;
    window.TextSelection.jsLog("nodes=" + len);

    if (len == 1) {
        var node = nodes[0];

        var r = document.createRange();
        r.setStart(node, startOffset);
        r.setEnd(node, endOffset);
        window.TextSelection.jsLog("r. only 1=" + r.toString());

        setRangeTextDecoration(r, textDecoration);
    } else {

        for (var i = 0; i < len; i++) {
            var node = nodes[i];
            var r = document.createRange();

            try {
                if (i == 0) {
                    r.setStart(node, startOffset);
                    r.setEnd(node, node.textContent.length -1);
                    window.TextSelection.jsLog("r. first=" + r.toString());

                    setRangeTextDecoration(r, textDecoration);
                } else if (i == len - 1) {
                    r.setStart(node, 0);
                    r.setEnd(node, endOffset);
                    window.TextSelection.jsLog("r. last=" + r.toString());

                    setRangeTextDecoration(r, textDecoration);
                } else if (i != 0 && i != len - 1) {
                    r.setStart(node, 0);
                    r.setEnd(node, node.textContent.length-1);
                    window.TextSelection.jsLog("r. middle=" + r.toString());

                    setRangeTextDecoration(r, textDecoration);
                }
            }catch(e){window.TextSelection.jsError("underline selection error " + e);}
        }
    }
}

function nextNode(node) {
    if (node.hasChildNodes()) {
        return node.firstChild;
    } else {
        while (node && !node.nextSibling) {
            node = node.parentNode;
        }
        if (!node) {
            return null;
        }
        return node.nextSibling;
    }
}

/**
*   Get all notes in range
*/
function getRangeNodes(range) {
    var node = range.startContainer;
    var endNode = range.endContainer;

// Special case for a range that is contained within a single node
    if (node == endNode) {
        return [node];
    }

// Iterate nodes until we hit the end container
    var rangeNodes = [];
    while (node && node != endNode) {
        rangeNodes.push( node = nextNode(node) );
    }

// Add partially selected nodes at the start of the range
    node = range.startContainer;
    while (node && node != range.commonAncestorContainer) {
        rangeNodes.unshift(node);
        node = node.parentNode;
    }

    return rangeNodes;
}

///
var rangeIntersectsNode = (typeof window.Range != "undefined"
        && Range.prototype.intersectsNode) ?

    function(range, node) {
        return range.intersectsNode(node);
    } :

    function(range, node) {
        var nodeRange = node.ownerDocument.createRange();
        try {
            nodeRange.selectNode(node);
        } catch (e) {
            nodeRange.selectNodeContents(node);
        }

        return range.compareBoundaryPoints(Range.END_TO_START, nodeRange) == -1 &&
            range.compareBoundaryPoints(Range.START_TO_END, nodeRange) == 1;
    };
    
/**
*   Get text nodes of range
*/
function getRangeTextNodes(range) {
// Create an array of all the text nodes in the selection
// using a TreeWalker
    var containerElement = range.commonAncestorContainer;
    if (containerElement.nodeType != 1) {
        containerElement = containerElement.parentNode;
    }

    var treeWalker = document.createTreeWalker(
        containerElement,
        NodeFilter.SHOW_TEXT,
// Note that Range.intersectsNode is non-standard but
// implemented in WebKit
        function(node) {
            return rangeIntersectsNode(range, node) ?
                NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT;
        },
        false
    );

    var selectedTextNodes = [];
    while (treeWalker.nextNode()) {
        selectedTextNodes.push(treeWalker.currentNode);
    }

    return selectedTextNodes;
}

/**
* Highlight selected text
*/
function highlight(colour) {
    var range, sel;
    if (window.getSelection) {
        // IE9 and non-IE
        try {
            if (!document.execCommand("BackColor", false, colour)) {
                makeEditableAndHighlight(colour);
            }
        } catch (ex) {
            makeEditableAndHighlight(colour)
        }
    } else if (document.selection && document.selection.createRange) {
        // IE <= 8 case
        range = document.selection.createRange();
        range.execCommand("BackColor", false, colour);
    }
}

/**
 * unhighlight all
 */
function unhighlightAll() {
    unhighlight(document);
}

/**
 * Unhighlight
 */
function unhighlight(node) {
    if (node.nodeType == 1) {
        node.style.backgroundColor = "";
        node.style.textDecoration = "initial";
    }
    var child = node.firstChild;
    while (child) {
        unhighlight(child);
        child = child.nextSibling;
    }
}

/**
* Save selection range start-end and color
*/
function doSave() {
    try {
        var savedSelection = saveSelection(document
            .getElementsByTagName('body')[0]);
        if (!savedSelection) {
            return;
        }
        savedSelection.color = currentColor;
        return savedSelection;
    } catch (e) {
        window.TextSelection.jsError("doSave\n" + e);
    }

}

function saveSelection(containerEl) {
    try {
        var sel = window.getSelection && window.getSelection();

        if (!sel || sel.rangCount == 0) {
            return;
        }

        if (sel && sel.rangeCount > 0) {
            var range = window.getSelection().getRangeAt(0);
            var preSelectionRange = range.cloneRange();
            preSelectionRange.selectNodeContents(containerEl);
//            preSelectionRange.setStart(range.startContainer, range.startOffset);
//            preSelectionRange.setEnd(range.endContainer, range.endOffset);
            preSelectionRange.setEnd(range.startContainer, range.startOffset);
            var start = preSelectionRange.toString().length;

            return {
                start: start,
                end: start + range.toString().length
            }
        }

    } catch (e) {
        window.TextSelection.jsError("saveSelection\n" + e);
    }
}

/**
* Change day/night mode
*/
function changeNightMode(isNightMode) {
    if (isNightMode) {
        $('body').css('background-color', 'black');
        $('body').css('color', 'white');
    } else {
        $('body').css('background-color', '');
        $('body').css('color', '');
    }
}

/**
* Get current selection color and font size
*/
function getSelectionColorAndFontSize() {
    var containerEl, sel;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            containerEl = sel.getRangeAt(0).commonAncestorContainer;
            // Make sure we have an element rather than a text node
            if (containerEl.nodeType == 3) {
                containerEl = containerEl.parentNode;
            }
        }
    } else if ((sel = document.selection) && sel.type != "Control") {
        containerEl = sel.createRange().parentElement();
    }

    if (containerEl) {
        var fontSize = getComputedStyleProperty(containerEl, "fontSize");
        var colour = getComputedStyleProperty(containerEl, "color");

        var res = {};
        res.color = colour;
        res.fontSize = fontSize;
        return res;
    }
}

function getComputedStyleProperty(el, propName) {
    if (window.getComputedStyle) {
        return window.getComputedStyle(el, null)[propName];
    } else if (el.currentStyle) {
        return el.currentStyle[propName];
    }
}

//============================= Note functions =======================================
var ic_note_src = 'file:///android_asset/ic_note_html.png';

/**
 * Insert note icon element by id
 * Each note icon is bounded in a tag with name icnote
 */
function insertNoteIcon(id) {
    var insertTag = '<sub><icnote id="' + id + '" z-index="1" position=absolute><img src="' + ic_note_src + '"/></icnote></sub>';

//  Check if already has a note icon with this is, do not add more
    var noteIcon = getNoteElementById(id);
    if (noteIcon) {
        return;
    }

    insertHtmlAfterSelection(insertTag);
    setNoteClickHandle(id);
//    android.selection.selectionChanged(false);
//    window.TextSelection.endSelectionMode();
}

/**
 * Remove note element by id
 */
function removeNoteElement(id) {
    try{
        var noteElm = getNoteElementById(id);
        if (!noteElm) {
            return;
        }

//      Remove parent of icnote, which is sub tag
        removeElement(noteElm.parentNode);
    } catch (e) {
        window.TextSelection.jsError("removeNoteElement\n" + e);
    }
}

/**
* Remove all note icon
*/
function removeAllNoteIcon() {
    removeTagsByName('icnote');
}

/**
* Remove all tags with name
*/
function removeTagsByName(name) {
    var elements = document.getElementsByTagName(name);
    while (elements[0]) {
        elements[0].parentNode.removeChild(elements[0]);
    }
}

/**
 * Get note element by id
 */
function getNoteElementById(id) {
    try {
        var noteElms = document.getElementsByTagName("icnote");

        for (var i = 0, len = noteElms.length; i < len; i++) {
            var noteElm = noteElms[i];

            var noteId = noteElm.getAttribute("id");
            if (noteId == id) {
                return noteElm;
            }
        }
    } catch (e) {
        window.TextSelection.jsError("getNoteElementById\n" + e);
    }
}

/**
 * Remove element from parent
 */
function removeElement(elm) {
    elm.parentNode.removeChild(elm);
}

/**
 * Set note element click handle by id
 */
function setNoteClickHandle(id) {
    try {
        var noteElm = getNoteElementById(id);

        if (!noteElm) {
            window.TextSelection.jsError("setNoteClickHandle undefined");
            return;
        }

        noteElm.addEventListener("click", function () {
            var noteId = noteElm.getAttribute("id");

            if (noteId !== undefined) {
                window.TextSelection.noteClicked(parseInt(noteId));
            }
        });
    } catch (e) {
        window.TextSelection.jsError("setNoteClickHandle\n" + e);
    }
}

// Insert before/after selection
//var insertHtmlBeforeSelection, insertHtmlAfterSelection;
//
//(function() {
//    function createInserter(isBefore) {
//        return function(html) {
//            var sel, range, node;
//            if (window.getSelection) {
//                // IE9 and non-IE
//                sel = window.getSelection();
//                if (sel.getRangeAt && sel.rangeCount) {
//                    range = window.getSelection().getRangeAt(0);
//
//                    range.collapse(isBefore);
//
//                    // Range.createContextualFragment() would be useful here but is
//                    // non-standard and not supported in all browsers (IE9, for one)
//                    var el = document.createElement("div");
//                    el.innerHTML = html;
//                    var frag = document.createDocumentFragment(), node, lastNode;
//                    while ( (node = el.firstChild) ) {
//                        lastNode = frag.appendChild(node);
//                    }
//                    range.insertNode(frag);
//                }
//            } else if (document.selection && document.selection.createRange) {
//                // IE < 9
//                range = document.selection.createRange();
//                range.collapse(isBefore);
//                range.pasteHTML(html);
//            }
//        }
//    }
//
//    insertHtmlBeforeSelection = createInserter(true);
//    insertHtmlAfterSelection = createInserter(false);
//})();

/**
 * Insert html tag after selection and clear selection
 */
function insertHtmlAfterSelection(html) {
    try {
        var sel, range, expandedSelRange, node;
        if (window.getSelection) {
            sel = window.getSelection();
            if (sel.getRangeAt && sel.rangeCount) {
                range = window.getSelection().getRangeAt(0);
                expandedSelRange = range.cloneRange();
                range.collapse(false);

                // Range.createContextualFragment() would be useful here but is
                // non-standard and not supported in all browsers (IE9, for one)
                var el = document.createElement("div");
                el.innerHTML = html;
                var frag = document.createDocumentFragment(), node, lastNode;
                while ((node = el.firstChild)) {
                    lastNode = frag.appendChild(node);
                }
                range.insertNode(frag);
                sel.removeAllRanges();

                // Preserve the selection
                if (lastNode) {
                    expandedSelRange.setEndAfter(lastNode);
//                    expandedSelRange.setStartBefore(lastNode);
                    sel.removeAllRanges();
                    sel.addRange(expandedSelRange);
                }
            }
        } else if (document.selection && document.selection.createRange) {
            range = document.selection.createRange();
            expandedSelRange = range.duplicate();
            range.collapse(false);
            range.pasteHTML(html);
            expandedSelRange.setEndPoint("EndToEnd", range);
            expandedSelRange.select();
        }
    } catch (e) {
        window.TextSelection.jsError("insertHtmlAfterSelection\n" + e);
    }
}

function snapSelectionToWord() {
    var sel;

    // Check for existence of window.getSelection() and that it has a
    // modify() method. IE 9 has both selection APIs but no modify() method.
    if (window.getSelection && (sel = window.getSelection()).modify) {
        sel = window.getSelection();
        if (!sel.isCollapsed) {

            // Detect if selection is backwards
            var range = document.createRange();
            range.setStart(sel.anchorNode, sel.anchorOffset);
            range.setEnd(sel.focusNode, sel.focusOffset);
            var backwards = range.collapsed;
            range.detach();

            // modify() works on the focus of the selection
            var endNode = sel.focusNode, endOffset = sel.focusOffset;
            sel.collapse(sel.anchorNode, sel.anchorOffset);
            if (backwards) {
                sel.modify("move", "backward", "character");
                sel.modify("move", "forward", "word");
                sel.extend(endNode, endOffset);
                sel.modify("extend", "forward", "character");
                sel.modify("extend", "backward", "word");

            } else {
                sel.modify("move", "forward", "character");
                sel.modify("move", "backward", "word");
                sel.extend(endNode, endOffset);
                sel.modify("extend", "backward", "character");
                sel.modify("extend", "forward", "word");
            }
        }
    } else if ( (sel = document.selection) && sel.type != "Control") {
        var textRange = sel.createRange();
        if (textRange.text) {
            textRange.expand("word");
            // Move the end back to not include the word's trailing space(s),
            // if necessary
            while (/\s$/.test(textRange.text)) {
                textRange.moveEnd("character", -1);
            }
            textRange.select();
        }
    }
}