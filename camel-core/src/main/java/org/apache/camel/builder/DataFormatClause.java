begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|model
operator|.
name|ProcessorType
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
name|dataformat
operator|.
name|ArtixDSContentType
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
name|dataformat
operator|.
name|ArtixDSDataFormat
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
name|dataformat
operator|.
name|CsvDataFormat
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
name|dataformat
operator|.
name|DataFormatType
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
name|dataformat
operator|.
name|JaxbDataFormat
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
name|dataformat
operator|.
name|SerializationDataFormat
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
name|dataformat
operator|.
name|StringDataFormat
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
name|dataformat
operator|.
name|XMLBeansDataFormat
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
name|dataformat
operator|.
name|XStreamDataFormat
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

begin_comment
comment|/**  * An expression for constructing the different possible {@link DataFormat}  * options.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DataFormatClause
specifier|public
class|class
name|DataFormatClause
parameter_list|<
name|T
extends|extends
name|ProcessorType
parameter_list|>
block|{
DECL|field|processorType
specifier|private
specifier|final
name|T
name|processorType
decl_stmt|;
DECL|field|operation
specifier|private
specifier|final
name|Operation
name|operation
decl_stmt|;
DECL|enum|Operation
specifier|public
enum|enum
name|Operation
block|{
DECL|enumConstant|Marshal
DECL|enumConstant|Unmarshal
name|Marshal
block|,
name|Unmarshal
block|}
empty_stmt|;
DECL|method|DataFormatClause (T processorType, Operation operation)
specifier|public
name|DataFormatClause
parameter_list|(
name|T
name|processorType
parameter_list|,
name|Operation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|processorType
operator|=
name|processorType
expr_stmt|;
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Uses the      *<a href="http://activemq.apache.org/camel/artix-data-services.html">Artix Data Services</a>      * data format for dealing with lots of different message formats such as SWIFT etc.      */
DECL|method|artixDS ()
specifier|public
name|T
name|artixDS
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://activemq.apache.org/camel/artix-data-services.html">Artix Data Services</a>      * data format with the specified type of ComplexDataObject      * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.      */
DECL|method|artixDS (Class<?> dataObjectType)
specifier|public
name|T
name|artixDS
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|(
name|dataObjectType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://activemq.apache.org/camel/artix-data-services.html">Artix Data Services</a>      * data format with the specified type of ComplexDataObject      * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.      */
DECL|method|artixDS (Class<?> elementType, ArtixDSContentType contentType)
specifier|public
name|T
name|artixDS
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|elementType
parameter_list|,
name|ArtixDSContentType
name|contentType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|(
name|elementType
argument_list|,
name|contentType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://activemq.apache.org/camel/artix-data-services.html">Artix Data Services</a>      * data format with the specified content type      * for marshalling and unmarshalling messages      */
DECL|method|artixDS (ArtixDSContentType contentType)
specifier|public
name|T
name|artixDS
parameter_list|(
name|ArtixDSContentType
name|contentType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|(
name|contentType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the CSV data format      */
DECL|method|csv ()
specifier|public
name|T
name|csv
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|CsvDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format      */
DECL|method|jaxb ()
specifier|public
name|T
name|jaxb
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JaxbDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format turning pretty printing on or off      */
DECL|method|jaxb (boolean prettyPrint)
specifier|public
name|T
name|jaxb
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JaxbDataFormat
argument_list|(
name|prettyPrint
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the Java Serialization data format      */
DECL|method|serialization ()
specifier|public
name|T
name|serialization
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SerializationDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the String data format      */
DECL|method|string ()
specifier|public
name|T
name|string
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|StringDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format      */
DECL|method|xmlBeans ()
specifier|public
name|T
name|xmlBeans
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|XMLBeansDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the XStream data format      */
DECL|method|xstream ()
specifier|public
name|T
name|xstream
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|XStreamDataFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|dataFormat (DataFormatType dataFormatType)
specifier|private
name|T
name|dataFormat
parameter_list|(
name|DataFormatType
name|dataFormatType
parameter_list|)
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|Unmarshal
case|:
return|return
operator|(
name|T
operator|)
name|processorType
operator|.
name|unmarshal
argument_list|(
name|dataFormatType
argument_list|)
return|;
case|case
name|Marshal
case|:
return|return
operator|(
name|T
operator|)
name|processorType
operator|.
name|marshal
argument_list|(
name|dataFormatType
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown value: "
operator|+
name|operation
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

