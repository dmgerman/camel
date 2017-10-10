begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_class
DECL|class|NullStrategy
specifier|public
class|class
name|NullStrategy
implements|implements
name|StringValueFromExchangeStrategy
block|{
annotation|@
name|Override
DECL|method|value (Exchange exchange)
specifier|public
name|String
name|value
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

