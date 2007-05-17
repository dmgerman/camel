begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_enum
DECL|enum|ConsumerType
specifier|public
enum|enum
name|ConsumerType
block|{
DECL|enumConstant|Simple
DECL|enumConstant|Default
DECL|enumConstant|ServerSessionPool
name|Simple
block|,
name|Default
block|,
name|ServerSessionPool
block|}
end_enum

end_unit

