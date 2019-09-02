begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.converters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
operator|.
name|converters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|converter
operator|.
name|IOConverter
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|BsonArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|BsonValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|BsonArrayCodec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|BsonValueCodecProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|DecoderContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|DocumentCodec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|DocumentCodecProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|ValueCodecProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|configuration
operator|.
name|CodecRegistries
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|codecs
operator|.
name|configuration
operator|.
name|CodecRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|conversions
operator|.
name|Bson
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|json
operator|.
name|JsonReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|MongoDbBasicConverters
specifier|public
specifier|final
class|class
name|MongoDbBasicConverters
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MongoDbBasicConverters
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MongoDbBasicConverters ()
specifier|private
name|MongoDbBasicConverters
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|fromMapToDocument (Map<String, Object> map)
specifier|public
specifier|static
name|Document
name|fromMapToDocument
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|Document
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromDocumentToMap (Document document)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fromDocumentToMap
parameter_list|(
name|Document
name|document
parameter_list|)
block|{
return|return
name|document
return|;
block|}
annotation|@
name|Converter
DECL|method|fromStringToDocument (String s)
specifier|public
specifier|static
name|Document
name|fromStringToDocument
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|Document
operator|.
name|parse
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromFileToDocument (File f, Exchange exchange)
specifier|public
specifier|static
name|Document
name|fromFileToDocument
parameter_list|(
name|File
name|f
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|fromInputStreamToDocument
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromInputStreamToDocument (InputStream is, Exchange exchange)
specifier|public
specifier|static
name|Document
name|fromInputStreamToDocument
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|input
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|isBson
argument_list|(
name|input
argument_list|)
condition|)
block|{
name|JsonReader
name|reader
init|=
operator|new
name|JsonReader
argument_list|(
operator|new
name|String
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentCodec
name|documentReader
init|=
operator|new
name|DocumentCodec
argument_list|()
decl_stmt|;
name|answer
operator|=
name|documentReader
operator|.
name|decode
argument_list|(
name|reader
argument_list|,
name|DecoderContext
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|Document
operator|.
name|parse
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|input
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// we need to make sure to close the input stream
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"InputStream"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * If the input starts with any number of whitespace characters and then a      * '{' character, we assume it is JSON rather than BSON. There are probably      * no useful BSON blobs that fit this pattern      */
DECL|method|isBson (byte[] input)
specifier|private
specifier|static
name|boolean
name|isBson
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|input
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|input
index|[
name|i
index|]
operator|==
literal|'{'
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|input
index|[
name|i
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|i
operator|++
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Converter
DECL|method|fromStringToList (String value)
specifier|public
specifier|static
name|List
argument_list|<
name|Bson
argument_list|>
name|fromStringToList
parameter_list|(
name|String
name|value
parameter_list|)
block|{
specifier|final
name|CodecRegistry
name|codecRegistry
init|=
name|CodecRegistries
operator|.
name|fromProviders
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ValueCodecProvider
argument_list|()
argument_list|,
operator|new
name|BsonValueCodecProvider
argument_list|()
argument_list|,
operator|new
name|DocumentCodecProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|JsonReader
name|reader
init|=
operator|new
name|JsonReader
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|BsonArrayCodec
name|arrayReader
init|=
operator|new
name|BsonArrayCodec
argument_list|(
name|codecRegistry
argument_list|)
decl_stmt|;
name|BsonArray
name|docArray
init|=
name|arrayReader
operator|.
name|decode
argument_list|(
name|reader
argument_list|,
name|DecoderContext
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Bson
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|docArray
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|BsonValue
name|doc
range|:
name|docArray
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|doc
operator|.
name|asDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

