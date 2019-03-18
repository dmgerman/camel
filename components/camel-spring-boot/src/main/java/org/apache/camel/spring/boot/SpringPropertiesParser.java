begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|component
operator|.
name|properties
operator|.
name|DefaultPropertiesParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|env
operator|.
name|PropertyResolver
import|;
end_import

begin_class
DECL|class|SpringPropertiesParser
class|class
name|SpringPropertiesParser
extends|extends
name|DefaultPropertiesParser
block|{
comment|// Members
annotation|@
name|Autowired
DECL|field|propertyResolver
specifier|private
name|PropertyResolver
name|propertyResolver
decl_stmt|;
comment|// Overridden
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, Properties properties)
specifier|public
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
return|return
name|propertyResolver
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit

