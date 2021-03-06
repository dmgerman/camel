begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|DataFormatDefinition
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * The BeanIO data format is used for working with flat payloads (such as CSV,  * delimited, or fixed length formats).  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,csv"
argument_list|,
name|title
operator|=
literal|"BeanIO"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"beanio"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BeanioDataFormat
specifier|public
class|class
name|BeanioDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|mapping
specifier|private
name|String
name|mapping
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|ignoreUnidentifiedRecords
specifier|private
name|String
name|ignoreUnidentifiedRecords
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|ignoreUnexpectedRecords
specifier|private
name|String
name|ignoreUnexpectedRecords
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|ignoreInvalidRecords
specifier|private
name|String
name|ignoreInvalidRecords
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|beanReaderErrorHandlerType
specifier|private
name|String
name|beanReaderErrorHandlerType
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|unmarshalSingleObject
specifier|private
name|String
name|unmarshalSingleObject
decl_stmt|;
DECL|method|BeanioDataFormat ()
specifier|public
name|BeanioDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"beanio"
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * The BeanIO mapping file. Is by default loaded from the classpath. You can      * prefix with file:, http:, or classpath: to denote from where to load the      * mapping file.      */
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
comment|/**      * The name of the stream to use.      */
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
DECL|method|getIgnoreUnidentifiedRecords ()
specifier|public
name|String
name|getIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|ignoreUnidentifiedRecords
return|;
block|}
comment|/**      * Whether to ignore unidentified records.      */
DECL|method|setIgnoreUnidentifiedRecords (String ignoreUnidentifiedRecords)
specifier|public
name|void
name|setIgnoreUnidentifiedRecords
parameter_list|(
name|String
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
DECL|method|getIgnoreUnexpectedRecords ()
specifier|public
name|String
name|getIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|ignoreUnexpectedRecords
return|;
block|}
comment|/**      * Whether to ignore unexpected records.      */
DECL|method|setIgnoreUnexpectedRecords (String ignoreUnexpectedRecords)
specifier|public
name|void
name|setIgnoreUnexpectedRecords
parameter_list|(
name|String
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
DECL|method|getIgnoreInvalidRecords ()
specifier|public
name|String
name|getIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|ignoreInvalidRecords
return|;
block|}
comment|/**      * Whether to ignore invalid records.      */
DECL|method|setIgnoreInvalidRecords (String ignoreInvalidRecords)
specifier|public
name|void
name|setIgnoreInvalidRecords
parameter_list|(
name|String
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
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * The charset to use.      *<p/>      * Is by default the JVM platform default charset.      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
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
comment|/**      * To use a custom org.apache.camel.dataformat.beanio.BeanIOErrorHandler as      * error handler while parsing. Configure the fully qualified class name of      * the error handler. Notice the options ignoreUnidentifiedRecords,      * ignoreUnexpectedRecords, and ignoreInvalidRecords may not be in use when      * you use a custom error handler.      */
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
DECL|method|getUnmarshalSingleObject ()
specifier|public
name|String
name|getUnmarshalSingleObject
parameter_list|()
block|{
return|return
name|unmarshalSingleObject
return|;
block|}
comment|/**      * This options controls whether to unmarshal as a list of objects or as a      * single object only. The former is the default mode, and the latter is      * only intended in special use-cases where beanio maps the Camel message to      * a single POJO bean.      */
DECL|method|setUnmarshalSingleObject (String unmarshalSingleObject)
specifier|public
name|void
name|setUnmarshalSingleObject
parameter_list|(
name|String
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

