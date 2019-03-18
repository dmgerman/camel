begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|internal
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
name|component
operator|.
name|olingo4
operator|.
name|Olingo4Configuration
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
name|support
operator|.
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * Singleton {@link ApiMethodPropertiesHelper} for Olingo4 component.  */
end_comment

begin_class
DECL|class|Olingo4PropertiesHelper
specifier|public
specifier|final
class|class
name|Olingo4PropertiesHelper
extends|extends
name|ApiMethodPropertiesHelper
argument_list|<
name|Olingo4Configuration
argument_list|>
block|{
DECL|field|helper
specifier|private
specifier|static
name|Olingo4PropertiesHelper
name|helper
decl_stmt|;
DECL|method|Olingo4PropertiesHelper ()
specifier|private
name|Olingo4PropertiesHelper
parameter_list|()
block|{
name|super
argument_list|(
name|Olingo4Configuration
operator|.
name|class
argument_list|,
name|Olingo4Constants
operator|.
name|PROPERTY_PREFIX
argument_list|)
expr_stmt|;
block|}
DECL|method|getHelper ()
specifier|public
specifier|static
specifier|synchronized
name|Olingo4PropertiesHelper
name|getHelper
parameter_list|()
block|{
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
name|helper
operator|=
operator|new
name|Olingo4PropertiesHelper
argument_list|()
expr_stmt|;
block|}
return|return
name|helper
return|;
block|}
block|}
end_class

end_unit

