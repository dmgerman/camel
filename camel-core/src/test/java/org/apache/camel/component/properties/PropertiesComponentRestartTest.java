begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|CamelContext
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
name|ContextTestSupport
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|SimpleRegistry
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PropertiesComponentRestartTest
specifier|public
class|class
name|PropertiesComponentRestartTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|resolvedCount
specifier|private
name|int
name|resolvedCount
decl_stmt|;
DECL|method|testPropertiesComponentCacheClearedOnStop ()
specifier|public
name|void
name|testPropertiesComponentCacheClearedOnStop
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resolvedCount
argument_list|)
expr_stmt|;
comment|// one cache miss
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resolvedCount
argument_list|)
expr_stmt|;
comment|// one more cache miss -- stop() cleared the cache
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:org/apache/camel/component/properties/myproperties.properties"
argument_list|)
decl_stmt|;
name|pc
operator|.
name|setPropertiesResolver
argument_list|(
operator|new
name|PropertiesResolver
argument_list|()
block|{
specifier|public
name|Properties
name|resolveProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
modifier|...
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|resolvedCount
operator|++
expr_stmt|;
return|return
operator|new
name|DefaultPropertiesResolver
argument_list|()
operator|.
name|resolveProperties
argument_list|(
name|context
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|uri
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// put the properties component into the registry so that it survives restarts
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
return|;
block|}
block|}
end_class

end_unit

