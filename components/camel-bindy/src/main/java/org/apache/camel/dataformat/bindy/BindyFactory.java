begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
package|;
end_package

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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * The bindy factory is a factory used to create the POJO models and bind or  * unbind the data to and from the record (CSV, ...)  */
end_comment

begin_interface
DECL|interface|BindyFactory
specifier|public
interface|interface
name|BindyFactory
block|{
comment|/**      * Prior to bind or unbind the data to and from string or model classes, the      * factory must create a collection of objects representing the model      *       * @throws Exception can be thrown      */
DECL|method|initModel ()
name|void
name|initModel
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * The bind allow to read the content of a record (expressed as a      * List<String>) and map it to the model classes.      *       * @param data List<String> represents the csv, ... data to transform      * @param model Map<String, object> is a collection of objects used to bind      *            data. String is the key name of the class link to POJO      *            objects      * @param line is the position of the record into the file      * @throws Exception can be thrown      */
DECL|method|bind (CamelContext camelContext, List<String> data, Map<String, Object> model, int line)
name|void
name|bind
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|data
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
parameter_list|,
name|int
name|line
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * The unbind is used to transform the content of the classes model objects      * into a string. The string represents a record of a CSV file      *       * @return String represents a csv record created      * @param model Map<String, Object> is a collection of objects used to      *            create csv, ... records. String is the key name of the      *            class link to POJO objects      * @throws Exception can be thrown      */
DECL|method|unbind (CamelContext camelContext, Map<String, Object> model)
name|String
name|unbind
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

