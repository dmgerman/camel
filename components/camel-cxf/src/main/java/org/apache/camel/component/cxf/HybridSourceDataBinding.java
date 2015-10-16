begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
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
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|DataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|DataWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|NodeDataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|NodeDataWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|SourceDataBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|XMLStreamDataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|source
operator|.
name|XMLStreamDataWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Fault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxb
operator|.
name|JAXBDataBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * This is a hybrid DataBinding of {@link JAXBDataBinding} and {@link org.apache.cxf.databinding.source.SourceDataBinding}.  * Like the SourceDataBinding, this DataBinding de/serializes parameters as DOMSource objects.  And like the JAXBDataBinding, the   * {@link #initialize(org.apache.cxf.service.Service)}  * method can initialize the service model's message part schema based on the service class in the message part info.    * Hence, this DataBinding supports DOMSource object de/serialization without requiring users to provide a WSDL.  *   * @version   */
end_comment

begin_class
DECL|class|HybridSourceDataBinding
specifier|public
class|class
name|HybridSourceDataBinding
extends|extends
name|JAXBDataBinding
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SourceDataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HybridSourceDataBinding ()
specifier|public
name|HybridSourceDataBinding
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|createReader (Class<T> cls)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataReader
argument_list|<
name|T
argument_list|>
name|createReader
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|XMLStreamReader
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|XMLStreamDataReader
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Node
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|NodeDataReader
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The type "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" is not supported."
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSupportedReaderFormats ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedReaderFormats
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|XMLStreamReader
operator|.
name|class
block|,
name|Node
operator|.
name|class
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|createWriter (Class<T> cls)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataWriter
argument_list|<
name|T
argument_list|>
name|createWriter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|XMLStreamWriter
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|XMLStreamDataWriter
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|write
argument_list|(
name|obj
argument_list|,
name|part
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Node
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|NodeDataWriter
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The type "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" is not supported."
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSupportedWriterFormats ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedWriterFormats
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|XMLStreamWriter
operator|.
name|class
block|,
name|Node
operator|.
name|class
block|}
return|;
block|}
block|}
end_class

end_unit

