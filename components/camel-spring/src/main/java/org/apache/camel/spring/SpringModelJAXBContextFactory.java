begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|DefaultModelJAXBContextFactory
import|;
end_import

begin_class
DECL|class|SpringModelJAXBContextFactory
specifier|public
class|class
name|SpringModelJAXBContextFactory
extends|extends
name|DefaultModelJAXBContextFactory
block|{
DECL|field|ADDITIONAL_JAXB_CONTEXT_PACKAGES
specifier|public
specifier|static
specifier|final
name|String
name|ADDITIONAL_JAXB_CONTEXT_PACKAGES
init|=
literal|":"
operator|+
literal|"org.apache.camel.core.xml:"
operator|+
literal|"org.apache.camel.spring:"
operator|+
literal|"org.apache.camel.util.spring:"
decl_stmt|;
annotation|@
name|Override
DECL|method|getPackages ()
specifier|protected
name|String
name|getPackages
parameter_list|()
block|{
return|return
name|super
operator|.
name|getPackages
argument_list|()
operator|+
name|ADDITIONAL_JAXB_CONTEXT_PACKAGES
return|;
block|}
block|}
end_class

end_unit

