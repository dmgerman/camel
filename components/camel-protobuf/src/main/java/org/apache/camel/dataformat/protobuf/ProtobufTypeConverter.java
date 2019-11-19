begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.dataformat.protobuf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|protobuf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_comment
comment|//@Converter(generateLoader = true)
end_comment

begin_class
DECL|class|ProtobufTypeConverter
specifier|public
class|class
name|ProtobufTypeConverter
block|{
comment|//@Converter
DECL|method|toMap (final Message message)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|toMap
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
return|return
name|ProtobufConverter
operator|.
name|toMap
argument_list|(
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

