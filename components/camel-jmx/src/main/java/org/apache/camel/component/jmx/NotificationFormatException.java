begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_comment
comment|/**  * Reports a problem formatting the Notification. This will be a JAXB related issue.  */
end_comment

begin_class
DECL|class|NotificationFormatException
specifier|public
class|class
name|NotificationFormatException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3460338958670572299L
decl_stmt|;
DECL|method|NotificationFormatException (Exception aCausedBy)
specifier|public
name|NotificationFormatException
parameter_list|(
name|Exception
name|aCausedBy
parameter_list|)
block|{
name|super
argument_list|(
name|aCausedBy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

