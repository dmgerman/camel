begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.health
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
operator|.
name|health
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
literal|"management.info.camel"
argument_list|)
DECL|class|HealthCheckVerboseConfiguration
specifier|public
class|class
name|HealthCheckVerboseConfiguration
block|{
comment|/**      * Global option to enable/disable  health bean camel version info, default is true.      */
DECL|field|verbose
specifier|private
name|boolean
name|verbose
init|=
literal|true
decl_stmt|;
DECL|method|isVerbose ()
specifier|public
name|boolean
name|isVerbose
parameter_list|()
block|{
return|return
name|verbose
return|;
block|}
DECL|method|setVerbose (boolean verbose)
specifier|public
name|void
name|setVerbose
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|this
operator|.
name|verbose
operator|=
name|verbose
expr_stmt|;
block|}
block|}
end_class

end_unit

