/**
 * @fileoverview Externs for the Panic Nova TextDocument attribute.
 * @externs
 */

/**
 * Namespace for Panic Nova.
 * @const
 */
var Nova = {};

/**
 * Constructor for a TextEditor.
 * @constructor
 */
Nova.TextEditor = function() {};

/**
 * The document associated with the TextEditor.
 * @type {Nova.TextDocument}
 */
Nova.TextEditor.prototype.document;


/**
 * The document associated with the TextEditor.
 * @type {Nova.Range}
 */
Nova.TextEditor.prototype.selectedRange;

/**
 * Constructor for a TextDocument.
 * @constructor
 */
Nova.TextDocument = function() {};


/**
 * The path of the document.
 * @type {string}
 */
Nova.TextDocument.prototype.path;

/**
 * The document associated with the TextEditor.
 * @param {Nova.Range} x
 * @return {Nova.Range}
 */
Nova.TextDocument.prototype.getLineRangeForRange = function(x) {};


/**
 * Constructor for a TextEditor.
 * @constructor
 */
Nova.Range = function() {};

/**
 * The start of the range.
 * @type {number}
 */
Nova.Range.prototype.start;

/**
 * The end of the range.
 * @type {number}
 */
Nova.Range.prototype.end;
