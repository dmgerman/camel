begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.jibx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|jibx
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

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|BindingDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|IBindingFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|IMarshallingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jibx
operator|.
name|runtime
operator|.
name|IUnmarshallingContext
import|;
end_import

begin_class
DECL|class|JibxDataFormat
specifier|public
class|class
name|JibxDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|unmarshallClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
decl_stmt|;
DECL|method|JibxDataFormat ()
specifier|public
name|JibxDataFormat
parameter_list|()
block|{     }
DECL|method|JibxDataFormat (Class<?> unmarshallClass)
specifier|public
name|JibxDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
operator|.
name|setUnmarshallClass
argument_list|(
name|unmarshallClass
argument_list|)
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|IBindingFactory
name|bindingFactory
init|=
name|BindingDirectory
operator|.
name|getFactory
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|IMarshallingContext
name|marshallingContext
init|=
name|bindingFactory
operator|.
name|createMarshallingContext
argument_list|()
decl_stmt|;
name|marshallingContext
operator|.
name|marshalDocument
argument_list|(
name|body
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getUnmarshallClass
argument_list|()
argument_list|,
literal|"unmarshallClass"
argument_list|)
expr_stmt|;
name|IBindingFactory
name|bindingFactory
init|=
name|BindingDirectory
operator|.
name|getFactory
argument_list|(
name|getUnmarshallClass
argument_list|()
argument_list|)
decl_stmt|;
name|IUnmarshallingContext
name|unmarshallingContext
init|=
name|bindingFactory
operator|.
name|createUnmarshallingContext
argument_list|()
decl_stmt|;
return|return
name|unmarshallingContext
operator|.
name|unmarshalDocument
argument_list|(
name|stream
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getUnmarshallClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshallClass
parameter_list|()
block|{
return|return
name|unmarshallClass
return|;
block|}
DECL|method|setUnmarshallClass (Class<?> unmarshallClass)
specifier|public
name|void
name|setUnmarshallClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
operator|.
name|unmarshallClass
operator|=
name|unmarshallClass
expr_stmt|;
block|}
block|}
end_class

end_unit

