begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.context
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|context
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
name|Converter
import|;
end_import

begin_comment
comment|/**  * Converts from a {@link CamelContext} to a {@link LocalContextComponent} so we can easily refer to  * external {@link CamelContext}s in the {@link org.apache.camel.spi.Registry} as a {@link org.apache.camel.Component}  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|ContextConverters
specifier|public
specifier|final
class|class
name|ContextConverters
block|{
DECL|method|ContextConverters ()
specifier|private
name|ContextConverters
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toComponent (CamelContext localContext)
specifier|public
specifier|static
name|LocalContextComponent
name|toComponent
parameter_list|(
name|CamelContext
name|localContext
parameter_list|)
block|{
return|return
operator|new
name|LocalContextComponent
argument_list|(
name|localContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

