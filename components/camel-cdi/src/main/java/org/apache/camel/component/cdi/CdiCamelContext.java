begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_comment
comment|/**  * CDI Camel Context class  * Register the CDIBeasnManager to lookup CDI Beans  * Can be used to produce a SimpleDefaultContext  */
end_comment

begin_class
DECL|class|CdiCamelContext
specifier|public
class|class
name|CdiCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|field|registry
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|CdiCamelContext ()
specifier|public
name|CdiCamelContext
parameter_list|()
block|{
name|this
operator|.
name|registry
operator|=
operator|new
name|CdiBeanRegistry
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Named
argument_list|(
literal|"simpleContext"
argument_list|)
annotation|@
name|Produces
DECL|method|createContext ()
specifier|public
name|DefaultCamelContext
name|createContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|DefaultCamelContext
operator|)
name|context
return|;
block|}
block|}
end_class

end_unit

