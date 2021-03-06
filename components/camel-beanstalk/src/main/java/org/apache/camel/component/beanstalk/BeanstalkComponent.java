begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
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

begin_comment
comment|/**  * Beanstalk Camel component.  *<p/>  * URI is<code>beanstalk://[host[:port]][/tube]?query</code>  *<p/>  * Parameters:<ul>  *<li><code>command</code> - one of "put", "release", "bury", "touch", "delete", "kick".  * "put" is the default for Producers.</li>  *<li><code>jobPriority</code></li>  *<li><code>jobDelay</code></li>  *<li><code>jobTimeToRun</code></li>  *<li><code>consumer.onFailure</code></li>  *<li><code>consumer.awaitJob</code></li>  *</ul>  *  * @see BeanstalkEndpoint  * @see ConnectionSettingsFactory  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"beanstalk"
argument_list|)
DECL|class|BeanstalkComponent
specifier|public
class|class
name|BeanstalkComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|DEFAULT_TUBE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_TUBE
init|=
literal|"default"
decl_stmt|;
DECL|field|COMMAND_BURY
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_BURY
init|=
literal|"bury"
decl_stmt|;
DECL|field|COMMAND_RELEASE
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_RELEASE
init|=
literal|"release"
decl_stmt|;
DECL|field|COMMAND_PUT
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_PUT
init|=
literal|"put"
decl_stmt|;
DECL|field|COMMAND_TOUCH
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_TOUCH
init|=
literal|"touch"
decl_stmt|;
DECL|field|COMMAND_DELETE
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_DELETE
init|=
literal|"delete"
decl_stmt|;
DECL|field|COMMAND_KICK
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_KICK
init|=
literal|"kick"
decl_stmt|;
DECL|field|DEFAULT_PRIORITY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_PRIORITY
init|=
literal|1000
decl_stmt|;
comment|// 0 is highest
DECL|field|DEFAULT_DELAY
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_DELAY
init|=
literal|0
decl_stmt|;
DECL|field|DEFAULT_TIME_TO_RUN
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_TIME_TO_RUN
init|=
literal|60
decl_stmt|;
comment|// if 0 the daemon sets 1.
DECL|field|connectionSettingsFactory
specifier|private
specifier|static
name|ConnectionSettingsFactory
name|connectionSettingsFactory
init|=
name|ConnectionSettingsFactory
operator|.
name|DEFAULT
decl_stmt|;
DECL|method|BeanstalkComponent ()
specifier|public
name|BeanstalkComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|useRawUri ()
specifier|public
name|boolean
name|useRawUri
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
return|return
operator|new
name|BeanstalkEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|connectionSettingsFactory
operator|.
name|parseUri
argument_list|(
name|remaining
argument_list|)
argument_list|,
name|remaining
argument_list|)
return|;
block|}
comment|/**      * Custom {@link ConnectionSettingsFactory}.      *<p/>      * Specify which {@link ConnectionSettingsFactory} to use to make connections to Beanstalkd. Especially      * useful for unit testing without beanstalkd daemon (you can mock {@link ConnectionSettings})      *      * @param connFactory the connection factory      * @see ConnectionSettingsFactory      */
DECL|method|setConnectionSettingsFactory (ConnectionSettingsFactory connFactory)
specifier|public
specifier|static
name|void
name|setConnectionSettingsFactory
parameter_list|(
name|ConnectionSettingsFactory
name|connFactory
parameter_list|)
block|{
name|BeanstalkComponent
operator|.
name|connectionSettingsFactory
operator|=
name|connFactory
expr_stmt|;
block|}
DECL|method|getConnectionSettingsFactory ()
specifier|public
specifier|static
name|ConnectionSettingsFactory
name|getConnectionSettingsFactory
parameter_list|()
block|{
return|return
name|connectionSettingsFactory
return|;
block|}
block|}
end_class

end_unit

