begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

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
name|Date
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
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|EventLog
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Box Event Logs Manager  *   *<p>  * Provides operations to read Box enterprise (admin) event logs.  *   *   *  */
end_comment

begin_class
DECL|class|BoxEventLogsManager
specifier|public
class|class
name|BoxEventLogsManager
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BoxEventLogsManager
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Box connection to authenticated user account.      */
DECL|field|boxConnection
specifier|private
name|BoxAPIConnection
name|boxConnection
decl_stmt|;
comment|/**      * Create event logs manager to manage the event logs of Box connection's      * authenticated user.      *       * @param boxConnection      *            - Box connection to authenticated user account.      */
DECL|method|BoxEventLogsManager (BoxAPIConnection boxConnection)
specifier|public
name|BoxEventLogsManager
parameter_list|(
name|BoxAPIConnection
name|boxConnection
parameter_list|)
block|{
name|this
operator|.
name|boxConnection
operator|=
name|boxConnection
expr_stmt|;
block|}
comment|/**      * Create an event stream with optional starting initial position and add      * listener that will be notified when an event is received.      *       * @param position      *            - the starting position of the event stream. May be      *<code>null</code> in which case all events within bounds      *            returned.      * @param after      *            - the lower bound on the timestamp of the events returned.      * @param before      *            - the upper bound on the timestamp of the events returned.      * @param types      *            - an optional list of event types to filter by.      *       * @return A list of all the events that met the given criteria.      */
DECL|method|getEnterpriseEvents (String position, Date after, Date before, BoxEvent.Type... types)
specifier|public
name|List
argument_list|<
name|BoxEvent
argument_list|>
name|getEnterpriseEvents
parameter_list|(
name|String
name|position
parameter_list|,
name|Date
name|after
parameter_list|,
name|Date
name|before
parameter_list|,
name|BoxEvent
operator|.
name|Type
modifier|...
name|types
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting all enterprise events occuring between "
operator|+
operator|(
name|after
operator|==
literal|null
condition|?
name|after
else|:
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|()
operator|.
name|format
argument_list|(
name|after
argument_list|)
operator|)
operator|+
literal|" and "
operator|+
operator|(
name|before
operator|==
literal|null
condition|?
name|before
else|:
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|()
operator|.
name|format
argument_list|(
name|before
argument_list|)
operator|)
operator|+
operator|(
name|position
operator|==
literal|null
condition|?
name|position
else|:
operator|(
literal|" starting at "
operator|+
name|position
operator|)
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|after
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'after' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|before
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'before' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|types
operator|==
literal|null
condition|)
block|{
name|types
operator|=
operator|new
name|BoxEvent
operator|.
name|Type
index|[
literal|0
index|]
expr_stmt|;
block|}
name|EventLog
name|eventLog
init|=
name|EventLog
operator|.
name|getEnterpriseEvents
argument_list|(
name|boxConnection
argument_list|,
name|position
argument_list|,
name|after
argument_list|,
name|before
argument_list|,
name|types
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BoxEvent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BoxEvent
name|event
range|:
name|eventLog
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

