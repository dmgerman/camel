begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.optaplanner
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|optaplanner
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
name|spi
operator|.
name|Metadata
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
name|UriParam
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|OptaPlannerConfiguration
specifier|public
class|class
name|OptaPlannerConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|configFile
specifier|private
name|String
name|configFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"DEFAULT_SOLVER"
argument_list|)
DECL|field|solverId
specifier|private
name|String
name|solverId
init|=
name|OptaPlannerConstants
operator|.
name|DEFAULT_SOLVER_ID
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|threadPoolSize
specifier|private
name|int
name|threadPoolSize
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|async
specifier|private
name|boolean
name|async
decl_stmt|;
DECL|method|getConfigFile ()
specifier|public
name|String
name|getConfigFile
parameter_list|()
block|{
return|return
name|configFile
return|;
block|}
comment|/**      * Specifies the location to the solver file      */
DECL|method|setConfigFile (String configFile)
specifier|public
name|void
name|setConfigFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|this
operator|.
name|configFile
operator|=
name|configFile
expr_stmt|;
block|}
DECL|method|getSolverId ()
specifier|public
name|String
name|getSolverId
parameter_list|()
block|{
return|return
name|solverId
return|;
block|}
comment|/**      * Specifies the solverId to user for the solver instance key      */
DECL|method|setSolverId (String solverId)
specifier|public
name|void
name|setSolverId
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
name|this
operator|.
name|solverId
operator|=
name|solverId
expr_stmt|;
block|}
DECL|method|getThreadPoolSize ()
specifier|public
name|int
name|getThreadPoolSize
parameter_list|()
block|{
return|return
name|threadPoolSize
return|;
block|}
comment|/**      * Specifies the thread pool size to use when async is true      */
DECL|method|setThreadPoolSize (int threadPoolSize)
specifier|public
name|void
name|setThreadPoolSize
parameter_list|(
name|int
name|threadPoolSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolSize
operator|=
name|threadPoolSize
expr_stmt|;
block|}
DECL|method|isAsync ()
specifier|public
name|boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
comment|/**      * Specifies to perform operations in async mode      */
DECL|method|setAsync (boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
block|}
end_class

end_unit

