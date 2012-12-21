begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileNotFoundException
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
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|util
operator|.
name|JSON
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|util
operator|.
name|JSONCallback
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
name|BSONCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|BasicBSONDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|ObjectMapper
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
specifier|transient
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
comment|// Jackson's ObjectMapper is thread-safe, so no need to create a pool nor synchronize access to it
DECL|field|objectMapper
specifier|private
specifier|static
name|ObjectMapper
name|objectMapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
DECL|method|MongoDbBasicConverters ()
specifier|private
name|MongoDbBasicConverters
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|fromMapToDBObject (Map<?, ?> map)
specifier|public
specifier|static
name|DBObject
name|fromMapToDBObject
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|BasicDBObject
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromBasicDBObjectToMap (BasicDBObject basicDbObject)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fromBasicDBObjectToMap
parameter_list|(
name|BasicDBObject
name|basicDbObject
parameter_list|)
block|{
return|return
name|basicDbObject
return|;
block|}
annotation|@
name|Converter
DECL|method|fromStringToDBObject (String s)
specifier|public
specifier|static
name|DBObject
name|fromStringToDBObject
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|DBObject
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
operator|(
name|DBObject
operator|)
name|JSON
operator|.
name|parse
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"String -> DBObject conversion selected, but the following exception occurred. Returning null."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Converter
DECL|method|fromFileToDBObject (File f, Exchange exchange)
specifier|public
specifier|static
name|DBObject
name|fromFileToDBObject
parameter_list|(
name|File
name|f
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|fromInputStreamToDBObject
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
DECL|method|fromInputStreamToDBObject (InputStream is, Exchange exchange)
specifier|public
specifier|static
name|DBObject
name|fromInputStreamToDBObject
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|DBObject
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
name|BSONCallback
name|callback
init|=
operator|new
name|JSONCallback
argument_list|()
decl_stmt|;
operator|new
name|BasicBSONDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|input
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|(
name|DBObject
operator|)
name|callback
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|(
name|DBObject
operator|)
name|JSON
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"String -> DBObject conversion selected, but the following exception occurred. Returning null."
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
comment|/**       * If the input starts with any number of whitespace characters and then a '{' character, we      * assume it is JSON rather than BSON. There are probably no useful BSON blobs that fit this pattern      */
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
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Converter
DECL|method|fromAnyObjectToDBObject (Object value)
specifier|public
specifier|static
name|DBObject
name|fromAnyObjectToDBObject
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|BasicDBObject
name|answer
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|m
init|=
name|MongoDbBasicConverters
operator|.
name|objectMapper
operator|.
name|convertValue
argument_list|(
name|value
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|BasicDBObject
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Conversion has fallen back to generic Object -> DBObject, but unable to convert type {}. Returning null."
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

