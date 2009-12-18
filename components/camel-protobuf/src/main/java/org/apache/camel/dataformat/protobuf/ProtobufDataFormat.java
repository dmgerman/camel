begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|Message
operator|.
name|Builder
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
name|CamelContext
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
name|CamelException
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
name|Exchange
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
name|InvalidPayloadException
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
name|spi
operator|.
name|DataFormat
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ProtobufDataFormat
specifier|public
class|class
name|ProtobufDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|defaultInstance
specifier|private
name|Message
name|defaultInstance
decl_stmt|;
DECL|field|instanceClassName
specifier|private
name|String
name|instanceClassName
decl_stmt|;
DECL|field|setDefaultInstanceHasBeenCalled
specifier|private
name|AtomicBoolean
name|setDefaultInstanceHasBeenCalled
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|/**      * @param defaultInstance      */
DECL|method|ProtobufDataFormat (Message defaultInstance)
specifier|public
name|ProtobufDataFormat
parameter_list|(
name|Message
name|defaultInstance
parameter_list|)
block|{
name|this
operator|.
name|defaultInstance
operator|=
name|defaultInstance
expr_stmt|;
block|}
DECL|method|ProtobufDataFormat ()
specifier|public
name|ProtobufDataFormat
parameter_list|()
block|{     }
DECL|method|setDefaultInstace (Message instance)
specifier|public
name|void
name|setDefaultInstace
parameter_list|(
name|Message
name|instance
parameter_list|)
block|{
name|this
operator|.
name|defaultInstance
operator|=
name|instance
expr_stmt|;
block|}
DECL|method|setDefaultInstance (Object instance)
specifier|public
name|void
name|setDefaultInstance
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|instanceof
name|Message
condition|)
block|{
name|this
operator|.
name|defaultInstance
operator|=
operator|(
name|Message
operator|)
name|instance
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The instance should be subClass of com.google.protobuf.Message"
argument_list|)
throw|;
block|}
block|}
DECL|method|setInstanceClass (String className)
specifier|public
name|void
name|setInstanceClass
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|className
argument_list|,
literal|"ProtobufDataFormat instaceClass"
argument_list|)
expr_stmt|;
name|instanceClassName
operator|=
name|className
expr_stmt|;
block|}
DECL|method|loadDefaultInstance (String className, CamelContext context)
specifier|protected
name|Message
name|loadDefaultInstance
parameter_list|(
name|String
name|className
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|CamelException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|Message
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|instanceClass
argument_list|)
condition|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|instanceClass
operator|.
name|getMethod
argument_list|(
literal|"getDefaultInstance"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
operator|(
name|Message
operator|)
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Can't set the defaultInstance of ProtobufferDataFormat with "
operator|+
name|className
operator|+
literal|", caused by "
operator|+
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Can't set the defaultInstance of ProtobufferDataFormat with "
operator|+
name|className
operator|+
literal|", as the class is not a subClass of com.google.protobuf.Message"
argument_list|)
throw|;
block|}
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.camel.spi.DataFormat#marshal(org.apache.camel.Exchange,      * java.lang.Object, java.io.OutputStream)      */
DECL|method|marshal (Exchange exchange, Object graph, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
operator|(
operator|(
name|Message
operator|)
name|graph
operator|)
operator|.
name|writeTo
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.camel.spi.DataFormat#unmarshal(org.apache.camel.Exchange,      * java.io.InputStream)      */
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|defaultInstance
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|instanceClassName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"There is not defaultInstance for protobuf unmarshaling"
argument_list|)
throw|;
block|}
else|else
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|setDefaultInstanceHasBeenCalled
operator|.
name|getAndSet
argument_list|(
literal|true
argument_list|)
condition|)
block|{
name|defaultInstance
operator|=
name|loadDefaultInstance
argument_list|(
name|instanceClassName
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Builder
name|builder
init|=
name|this
operator|.
name|defaultInstance
operator|.
name|newBuilderForType
argument_list|()
operator|.
name|mergeFrom
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|builder
operator|.
name|isInitialized
argument_list|()
condition|)
block|{
comment|// TODO which exception should be thrown here?
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|this
operator|.
name|defaultInstance
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

