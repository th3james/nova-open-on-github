// panic_nova_externs.js

/** @externs */

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
 * Constructor for a TextDocument.
 * @constructor
 */
Nova.TextDocument = function() {};

/**
 * The text of the document.
 * @type {string}
 */
Nova.TextDocument.prototype.text;

/**
 * The path of the document.
 * @type {string}
 */
Nova.TextDocument.prototype.path;

/**
 * Constructor for a TextEditor.
 * @constructor
 */
Nova.Range = function() {};

/**
 * The document associated with the TextEditor.
 * @type {Nova.TextDocument}
 */
Nova.TextEditor.prototype.document;
