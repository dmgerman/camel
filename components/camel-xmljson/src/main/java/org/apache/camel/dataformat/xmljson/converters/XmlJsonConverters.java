begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson.converters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmljson
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
name|IOException
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
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSON
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

begin_comment
comment|/**  * Contains type converters to cater for Camel's unconditional conversion of the message body to an   * InputStream prior to marshaling  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|XmlJsonConverters
specifier|public
specifier|final
class|class
name|XmlJsonConverters
block|{
DECL|method|XmlJsonConverters ()
specifier|private
name|XmlJsonConverters
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Converts from an existing JSON object circulating as such to an      * InputStream, by dumping it to a String first and then using camel-core's      * {@link IOConverter#toInputStream(String, Exchange)}      */
annotation|@
name|Converter
DECL|method|fromJSONtoInputStream (JSON json, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|fromJSONtoInputStream
parameter_list|(
name|JSON
name|json
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|json
operator|.
name|toString
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

