begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.builder.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|saxon
package|;
end_package

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_enum
DECL|enum|ResultFormat
specifier|public
enum|enum
name|ResultFormat
block|{
DECL|enumConstant|Bytes
DECL|enumConstant|BytesSource
DECL|enumConstant|DOM
DECL|enumConstant|DOMSource
DECL|enumConstant|List
DECL|enumConstant|String
DECL|enumConstant|StringSource
name|Bytes
block|,
name|BytesSource
block|,
name|DOM
block|,
name|DOMSource
block|,
name|List
block|,
name|String
block|,
name|StringSource
block|}
end_enum

end_unit

