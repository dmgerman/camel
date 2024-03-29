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
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Client
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
name|AsyncEndpoint
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
name|Processor
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
name|Producer
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|BuryCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|Command
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|DeleteCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|KickCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|PutCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|ReleaseCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|TouchCommand
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
name|UriEndpoint
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
name|UriPath
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
name|ScheduledPollEndpoint
import|;
end_import

begin_comment
comment|/**  * The beanstalk component is used for job retrieval and post-processing of Beanstalk jobs.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.15.0"
argument_list|,
name|scheme
operator|=
literal|"beanstalk"
argument_list|,
name|title
operator|=
literal|"Beanstalk"
argument_list|,
name|syntax
operator|=
literal|"beanstalk:connectionSettings"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|BeanstalkEndpoint
specifier|public
class|class
name|BeanstalkEndpoint
extends|extends
name|ScheduledPollEndpoint
implements|implements
name|AsyncEndpoint
block|{
DECL|field|conn
specifier|final
name|ConnectionSettings
name|conn
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Connection settings host:port/tube"
argument_list|)
DECL|field|connectionSettings
specifier|private
name|String
name|connectionSettings
decl_stmt|;
annotation|@
name|UriParam
DECL|field|command
specifier|private
name|BeanstalkCommand
name|command
init|=
name|BeanstalkCommand
operator|.
name|put
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|BeanstalkComponent
operator|.
name|DEFAULT_PRIORITY
argument_list|)
DECL|field|jobPriority
specifier|private
name|long
name|jobPriority
init|=
name|BeanstalkComponent
operator|.
name|DEFAULT_PRIORITY
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|BeanstalkComponent
operator|.
name|DEFAULT_DELAY
argument_list|)
DECL|field|jobDelay
specifier|private
name|int
name|jobDelay
init|=
name|BeanstalkComponent
operator|.
name|DEFAULT_DELAY
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|BeanstalkComponent
operator|.
name|DEFAULT_TIME_TO_RUN
argument_list|)
DECL|field|jobTimeToRun
specifier|private
name|int
name|jobTimeToRun
init|=
name|BeanstalkComponent
operator|.
name|DEFAULT_TIME_TO_RUN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|onFailure
specifier|private
name|BeanstalkCommand
name|onFailure
init|=
name|BeanstalkCommand
operator|.
name|bury
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useBlockIO
specifier|private
name|boolean
name|useBlockIO
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|awaitJob
specifier|private
name|boolean
name|awaitJob
init|=
literal|true
decl_stmt|;
DECL|method|BeanstalkEndpoint (final String uri, final Component component, final ConnectionSettings conn, final String connectionSettings)
specifier|public
name|BeanstalkEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|Component
name|component
parameter_list|,
specifier|final
name|ConnectionSettings
name|conn
parameter_list|,
specifier|final
name|String
name|connectionSettings
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|conn
operator|=
name|conn
expr_stmt|;
name|this
operator|.
name|connectionSettings
operator|=
name|connectionSettings
expr_stmt|;
block|}
DECL|method|getConnectionSettings ()
specifier|public
name|String
name|getConnectionSettings
parameter_list|()
block|{
return|return
name|connectionSettings
return|;
block|}
DECL|method|getConnection ()
specifier|public
name|ConnectionSettings
name|getConnection
parameter_list|()
block|{
return|return
name|conn
return|;
block|}
DECL|method|getConn ()
specifier|public
name|ConnectionSettings
name|getConn
parameter_list|()
block|{
return|return
name|conn
return|;
block|}
DECL|method|getCommand ()
specifier|public
name|BeanstalkCommand
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
comment|/**      * put means to put the job into Beanstalk. Job body is specified in the Camel message body. Job ID will be returned in beanstalk.jobId message header.      * delete, release, touch or bury expect Job ID in the message header beanstalk.jobId. Result of the operation is returned in beanstalk.result message header      * kick expects the number of jobs to kick in the message body and returns the number of jobs actually kicked out in the message header beanstalk.result.      */
DECL|method|setCommand (BeanstalkCommand command)
specifier|public
name|void
name|setCommand
parameter_list|(
name|BeanstalkCommand
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
DECL|method|getJobPriority ()
specifier|public
name|long
name|getJobPriority
parameter_list|()
block|{
return|return
name|jobPriority
return|;
block|}
comment|/**      * Job priority. (0 is the highest, see Beanstalk protocol)      */
DECL|method|setJobPriority (long jobPriority)
specifier|public
name|void
name|setJobPriority
parameter_list|(
name|long
name|jobPriority
parameter_list|)
block|{
name|this
operator|.
name|jobPriority
operator|=
name|jobPriority
expr_stmt|;
block|}
DECL|method|getJobDelay ()
specifier|public
name|int
name|getJobDelay
parameter_list|()
block|{
return|return
name|jobDelay
return|;
block|}
comment|/**      * Job delay in seconds.      */
DECL|method|setJobDelay (int jobDelay)
specifier|public
name|void
name|setJobDelay
parameter_list|(
name|int
name|jobDelay
parameter_list|)
block|{
name|this
operator|.
name|jobDelay
operator|=
name|jobDelay
expr_stmt|;
block|}
DECL|method|getJobTimeToRun ()
specifier|public
name|int
name|getJobTimeToRun
parameter_list|()
block|{
return|return
name|jobTimeToRun
return|;
block|}
comment|/**      * Job time to run in seconds. (when 0, the beanstalkd daemon raises it to 1 automatically, see Beanstalk protocol)      */
DECL|method|setJobTimeToRun (int jobTimeToRun)
specifier|public
name|void
name|setJobTimeToRun
parameter_list|(
name|int
name|jobTimeToRun
parameter_list|)
block|{
name|this
operator|.
name|jobTimeToRun
operator|=
name|jobTimeToRun
expr_stmt|;
block|}
DECL|method|getOnFailure ()
specifier|public
name|BeanstalkCommand
name|getOnFailure
parameter_list|()
block|{
return|return
name|onFailure
return|;
block|}
comment|/**      * Command to use when processing failed.      */
DECL|method|setOnFailure (BeanstalkCommand onFailure)
specifier|public
name|void
name|setOnFailure
parameter_list|(
name|BeanstalkCommand
name|onFailure
parameter_list|)
block|{
name|this
operator|.
name|onFailure
operator|=
name|onFailure
expr_stmt|;
block|}
DECL|method|isUseBlockIO ()
specifier|public
name|boolean
name|isUseBlockIO
parameter_list|()
block|{
return|return
name|useBlockIO
return|;
block|}
comment|/**      * Whether to use blockIO.      */
DECL|method|setUseBlockIO (boolean useBlockIO)
specifier|public
name|void
name|setUseBlockIO
parameter_list|(
name|boolean
name|useBlockIO
parameter_list|)
block|{
name|this
operator|.
name|useBlockIO
operator|=
name|useBlockIO
expr_stmt|;
block|}
DECL|method|isAwaitJob ()
specifier|public
name|boolean
name|isAwaitJob
parameter_list|()
block|{
return|return
name|awaitJob
return|;
block|}
comment|/**      * Whether to wait for job to complete before ack the job from beanstalk      */
DECL|method|setAwaitJob (boolean awaitJob)
specifier|public
name|void
name|setAwaitJob
parameter_list|(
name|boolean
name|awaitJob
parameter_list|)
block|{
name|this
operator|.
name|awaitJob
operator|=
name|awaitJob
expr_stmt|;
block|}
comment|/**      * Creates Camel producer.      *<p/>      * Depending on the command parameter (see {@link BeanstalkComponent} URI) it      * will create one of the producer implementations.      *      * @return {@link Producer} instance      * @throws IllegalArgumentException when {@link ConnectionSettings} cannot      *                                  create a writable {@link Client}      */
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Command
name|cmd
decl_stmt|;
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_PUT
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|PutCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_RELEASE
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|ReleaseCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_BURY
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|BuryCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_TOUCH
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|TouchCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_DELETE
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|DeleteCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_KICK
operator|.
name|equals
argument_list|(
name|command
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|cmd
operator|=
operator|new
name|KickCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown command for Beanstalk endpoint: %s"
argument_list|,
name|command
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|new
name|BeanstalkProducer
argument_list|(
name|this
argument_list|,
name|cmd
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|BeanstalkConsumer
name|consumer
init|=
operator|new
name|BeanstalkConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setAwaitJob
argument_list|(
name|isAwaitJob
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setOnFailure
argument_list|(
name|getOnFailure
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setUseBlockIO
argument_list|(
name|isUseBlockIO
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
block|}
end_class

end_unit

