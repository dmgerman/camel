begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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

begin_comment
comment|/**  * A number of helper methods for working with collections  *   * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|CollectionHelper
specifier|public
class|class
name|CollectionHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CollectionHelper ()
specifier|private
name|CollectionHelper
parameter_list|()
block|{             }
comment|/**      * Sets the value of the entry in the map for the given key, though if the      * map already contains a value for the given key then the value is appended      * to a list of values.      *       * @param map the map to add the entry to      * @param key the key in the map      * @param value the value to put in the map      */
DECL|method|appendValue (Map map, Object key, Object value)
specifier|public
specifier|static
name|void
name|appendValue
parameter_list|(
name|Map
name|map
parameter_list|,
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Object
name|oldValue
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|List
name|list
decl_stmt|;
if|if
condition|(
name|oldValue
operator|instanceof
name|List
condition|)
block|{
name|list
operator|=
operator|(
name|List
operator|)
name|oldValue
expr_stmt|;
block|}
else|else
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

