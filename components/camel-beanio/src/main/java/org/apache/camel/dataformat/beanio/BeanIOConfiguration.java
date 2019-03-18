begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|BeanReaderErrorHandler
import|;
end_import

begin_comment
comment|/**  * To configure the BeanIO data format, or BeanIO splitter.  */
end_comment

begin_class
DECL|class|BeanIOConfiguration
specifier|public
class|class
name|BeanIOConfiguration
block|{
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
DECL|field|mapping
specifier|private
name|String
name|mapping
decl_stmt|;
DECL|field|ignoreUnidentifiedRecords
specifier|private
name|boolean
name|ignoreUnidentifiedRecords
decl_stmt|;
DECL|field|ignoreUnexpectedRecords
specifier|private
name|boolean
name|ignoreUnexpectedRecords
decl_stmt|;
DECL|field|ignoreInvalidRecords
specifier|private
name|boolean
name|ignoreInvalidRecords
decl_stmt|;
DECL|field|encoding
specifier|private
name|Charset
name|encoding
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
decl_stmt|;
DECL|field|beanReaderErrorHandler
specifier|private
name|BeanReaderErrorHandler
name|beanReaderErrorHandler
decl_stmt|;
DECL|field|beanReaderErrorHandlerType
specifier|private
name|String
name|beanReaderErrorHandlerType
decl_stmt|;
DECL|field|unmarshalSingleObject
specifier|private
name|boolean
name|unmarshalSingleObject
decl_stmt|;
DECL|method|getMapping ()
specifier|public
name|String
name|getMapping
parameter_list|()
block|{
return|return
name|mapping
return|;
block|}
DECL|method|setMapping (String mapping)
specifier|public
name|void
name|setMapping
parameter_list|(
name|String
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|streamName
return|;
block|}
DECL|method|setStreamName (String streamName)
specifier|public
name|void
name|setStreamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
DECL|method|isIgnoreUnidentifiedRecords ()
specifier|public
name|boolean
name|isIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|ignoreUnidentifiedRecords
return|;
block|}
DECL|method|setIgnoreUnidentifiedRecords (boolean ignoreUnidentifiedRecords)
specifier|public
name|void
name|setIgnoreUnidentifiedRecords
parameter_list|(
name|boolean
name|ignoreUnidentifiedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnidentifiedRecords
operator|=
name|ignoreUnidentifiedRecords
expr_stmt|;
block|}
DECL|method|isIgnoreUnexpectedRecords ()
specifier|public
name|boolean
name|isIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|ignoreUnexpectedRecords
return|;
block|}
DECL|method|setIgnoreUnexpectedRecords (boolean ignoreUnexpectedRecords)
specifier|public
name|void
name|setIgnoreUnexpectedRecords
parameter_list|(
name|boolean
name|ignoreUnexpectedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnexpectedRecords
operator|=
name|ignoreUnexpectedRecords
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidRecords ()
specifier|public
name|boolean
name|isIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|ignoreInvalidRecords
return|;
block|}
DECL|method|setIgnoreInvalidRecords (boolean ignoreInvalidRecords)
specifier|public
name|void
name|setIgnoreInvalidRecords
parameter_list|(
name|boolean
name|ignoreInvalidRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidRecords
operator|=
name|ignoreInvalidRecords
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|Charset
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (Charset encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|Charset
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getBeanReaderErrorHandler ()
specifier|public
name|BeanReaderErrorHandler
name|getBeanReaderErrorHandler
parameter_list|()
block|{
return|return
name|beanReaderErrorHandler
return|;
block|}
DECL|method|setBeanReaderErrorHandler (BeanReaderErrorHandler beanReaderErrorHandler)
specifier|public
name|void
name|setBeanReaderErrorHandler
parameter_list|(
name|BeanReaderErrorHandler
name|beanReaderErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|beanReaderErrorHandler
operator|=
name|beanReaderErrorHandler
expr_stmt|;
block|}
DECL|method|getBeanReaderErrorHandlerType ()
specifier|public
name|String
name|getBeanReaderErrorHandlerType
parameter_list|()
block|{
return|return
name|beanReaderErrorHandlerType
return|;
block|}
DECL|method|setBeanReaderErrorHandlerType (String beanReaderErrorHandlerType)
specifier|public
name|void
name|setBeanReaderErrorHandlerType
parameter_list|(
name|String
name|beanReaderErrorHandlerType
parameter_list|)
block|{
name|this
operator|.
name|beanReaderErrorHandlerType
operator|=
name|beanReaderErrorHandlerType
expr_stmt|;
block|}
DECL|method|setBeanReaderErrorHandlerType (Class<?> beanReaderErrorHandlerType)
specifier|public
name|void
name|setBeanReaderErrorHandlerType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanReaderErrorHandlerType
parameter_list|)
block|{
name|this
operator|.
name|beanReaderErrorHandlerType
operator|=
name|beanReaderErrorHandlerType
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
DECL|method|isUnmarshalSingleObject ()
specifier|public
name|boolean
name|isUnmarshalSingleObject
parameter_list|()
block|{
return|return
name|unmarshalSingleObject
return|;
block|}
DECL|method|setUnmarshalSingleObject (boolean unmarshalSingleObject)
specifier|public
name|void
name|setUnmarshalSingleObject
parameter_list|(
name|boolean
name|unmarshalSingleObject
parameter_list|)
block|{
name|this
operator|.
name|unmarshalSingleObject
operator|=
name|unmarshalSingleObject
expr_stmt|;
block|}
block|}
end_class

end_unit

