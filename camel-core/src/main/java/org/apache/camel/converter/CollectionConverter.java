begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * Some core java.util Collection based  *<a href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a>  *  * @version $Revision: 524215 $  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CollectionConverter
specifier|public
class|class
name|CollectionConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CollectionConverter ()
specifier|private
name|CollectionConverter
parameter_list|()
block|{             }
comment|/**      * Converts a collection to an array      */
annotation|@
name|Converter
DECL|method|toArray (Collection value)
specifier|public
specifier|static
name|Object
index|[]
name|toArray
parameter_list|(
name|Collection
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|toArray
argument_list|()
return|;
block|}
comment|/**      * Converts an array to a collection      */
annotation|@
name|Converter
DECL|method|toList (Object[] array)
specifier|public
specifier|static
name|List
name|toList
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toSet (Object[] array)
specifier|public
specifier|static
name|Set
name|toSet
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|Set
name|answer
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|element
range|:
name|array
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Converter
DECL|method|toSet (Collection collection)
specifier|public
specifier|static
name|Set
name|toSet
parameter_list|(
name|Collection
name|collection
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toSet (Map map)
specifier|public
specifier|static
name|Set
name|toSet
parameter_list|(
name|Map
name|map
parameter_list|)
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

