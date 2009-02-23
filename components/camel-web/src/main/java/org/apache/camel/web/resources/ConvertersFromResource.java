begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
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
name|impl
operator|.
name|converter
operator|.
name|DefaultTypeConverter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ConvertersFromResource
specifier|public
class|class
name|ConvertersFromResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|field|type
specifier|private
name|Class
name|type
decl_stmt|;
DECL|method|ConvertersFromResource (CamelContextResource contextResource, Class type)
specifier|public
name|ConvertersFromResource
parameter_list|(
name|CamelContextResource
name|contextResource
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|contextResource
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getConverters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|getConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|answer
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
argument_list|()
decl_stmt|;
name|DefaultTypeConverter
name|converter
init|=
name|getDefaultTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|converter
operator|.
name|getToClassMappings
argument_list|(
name|type
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
name|aClass
range|:
name|classes
control|)
block|{
name|String
name|name
init|=
name|ConvertersResource
operator|.
name|nameOf
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

