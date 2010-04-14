begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * Authentication policy  *  * @version $Revision$  */
end_comment

begin_enum
DECL|enum|AuthMethod
specifier|public
enum|enum
name|AuthMethod
block|{
DECL|enumConstant|Basic
DECL|enumConstant|Digest
DECL|enumConstant|NTML
name|Basic
block|,
name|Digest
block|,
name|NTML
block|; }
end_enum

end_unit

