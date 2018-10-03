begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * A very simple stop watch.  *<p/>  * This implementation is not thread safe and can only time one task at any given time.  */
end_comment

begin_class
DECL|class|StopWatch
specifier|public
specifier|final
class|class
name|StopWatch
block|{
DECL|field|start
specifier|private
name|long
name|start
decl_stmt|;
comment|/**      * Starts the stop watch      */
DECL|method|StopWatch ()
specifier|public
name|StopWatch
parameter_list|()
block|{
name|this
operator|.
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
comment|/**      * Starts the stop watch from the given timestamp      */
DECL|method|StopWatch (Date startTimestamp)
specifier|public
name|StopWatch
parameter_list|(
name|Date
name|startTimestamp
parameter_list|)
block|{
name|start
operator|=
name|startTimestamp
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates the stop watch      *      * @param start whether it should start immediately      */
DECL|method|StopWatch (boolean start)
specifier|public
name|StopWatch
parameter_list|(
name|boolean
name|start
parameter_list|)
block|{
if|if
condition|(
name|start
condition|)
block|{
name|this
operator|.
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Starts or restarts the stop watch      */
DECL|method|restart ()
specifier|public
name|void
name|restart
parameter_list|()
block|{
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the time taken in millis.      *      * @return time in millis, or<tt>0</tt> if not started yet.      */
DECL|method|taken ()
specifier|public
name|long
name|taken
parameter_list|()
block|{
if|if
condition|(
name|start
operator|>
literal|0
condition|)
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

