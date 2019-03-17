begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jooq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"jooq"
argument_list|)
DECL|class|JooqComponent
specifier|public
class|class
name|JooqComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Component configuration (database connection, database entity type, etc.)"
argument_list|)
DECL|field|configuration
specifier|private
name|JooqConfiguration
name|configuration
decl_stmt|;
DECL|method|JooqComponent ()
specifier|public
name|JooqComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|JooqConfiguration
name|conf
init|=
name|configuration
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|copy
argument_list|()
else|:
operator|new
name|JooqConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|conf
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|JooqEndpoint
name|endpoint
init|=
operator|new
name|JooqEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|,
name|conf
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|setConfiguration (JooqConfiguration jooqConfiguration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JooqConfiguration
name|jooqConfiguration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|jooqConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit
