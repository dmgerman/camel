begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|Consumer
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
name|ServiceStatus
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
name|StatefulService
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
name|StaticService
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
name|RestRegistry
import|;
end_import

begin_class
DECL|class|DefaultRestRegistry
specifier|public
class|class
name|DefaultRestRegistry
extends|extends
name|ServiceSupport
implements|implements
name|StaticService
implements|,
name|RestRegistry
block|{
DECL|field|registry
specifier|private
specifier|final
name|Map
argument_list|<
name|Consumer
argument_list|,
name|RestService
argument_list|>
name|registry
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Consumer
argument_list|,
name|RestService
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|addRestService (Consumer consumer, String url, String method, String uriTemplate, String consumes, String produces)
specifier|public
name|void
name|addRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|)
block|{
name|RestServiceEntry
name|entry
init|=
operator|new
name|RestServiceEntry
argument_list|(
name|consumer
argument_list|,
name|url
argument_list|,
name|uriTemplate
argument_list|,
name|method
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|)
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
DECL|method|removeRestService (Consumer consumer)
specifier|public
name|void
name|removeRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|registry
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listAllRestServices ()
specifier|public
name|List
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
name|listAllRestServices
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|RestService
argument_list|>
argument_list|(
name|registry
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|registry
operator|.
name|size
argument_list|()
return|;
block|}
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Represents a rest service      */
DECL|class|RestServiceEntry
specifier|private
specifier|final
class|class
name|RestServiceEntry
implements|implements
name|RestService
block|{
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
name|consumer
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|verb
specifier|private
specifier|final
name|String
name|verb
decl_stmt|;
DECL|field|consumes
specifier|private
specifier|final
name|String
name|consumes
decl_stmt|;
DECL|field|produces
specifier|private
specifier|final
name|String
name|produces
decl_stmt|;
DECL|method|RestServiceEntry (Consumer consumer, String url, String path, String verb, String consumes, String produces)
specifier|private
name|RestServiceEntry
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
block|}
DECL|method|getConsumer ()
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getUriTemplate ()
specifier|public
name|String
name|getUriTemplate
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|verb
return|;
block|}
DECL|method|getConsumes ()
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumes
return|;
block|}
DECL|method|getProduces ()
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|produces
return|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
name|ServiceStatus
name|status
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|consumer
operator|instanceof
name|StatefulService
condition|)
block|{
name|status
operator|=
operator|(
operator|(
name|StatefulService
operator|)
name|consumer
operator|)
operator|.
name|getStatus
argument_list|()
expr_stmt|;
block|}
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

