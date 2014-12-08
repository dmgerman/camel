begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|support
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|deltaspike
operator|.
name|core
operator|.
name|api
operator|.
name|config
operator|.
name|PropertyFileConfig
import|;
end_import

begin_comment
comment|/**  * Class which is used to retrieve camel properties files tp configure endpoints.  * By default, it will check for the file META-INF/camel-properties  */
end_comment

begin_class
DECL|class|CdiConfigFile
specifier|public
class|class
name|CdiConfigFile
implements|implements
name|PropertyFileConfig
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
DECL|method|getPropertyFileName ()
specifier|public
name|String
name|getPropertyFileName
parameter_list|()
block|{
return|return
literal|"META-INF/camel.properties"
return|;
block|}
annotation|@
name|Override
DECL|method|isOptional ()
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

