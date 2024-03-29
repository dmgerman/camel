begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * This class provides utility methods  */
end_comment

begin_class
DECL|class|TestUtils
specifier|final
class|class
name|TestUtils
block|{
DECL|method|TestUtils ()
specifier|private
name|TestUtils
parameter_list|()
block|{
comment|// Prevent instantiation
block|}
comment|/**      * Create a map with the given key/value pairs      *      * @param strings key/value pairs      * @return Map with the given key/value pairs      */
DECL|method|asMap (String... strings)
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|asMap
parameter_list|(
name|String
modifier|...
name|strings
parameter_list|)
block|{
if|if
condition|(
name|strings
operator|.
name|length
operator|%
literal|2
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create a map with an add number of strings"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|strings
operator|.
name|length
operator|/
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|strings
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|strings
index|[
name|i
index|]
argument_list|,
name|strings
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

