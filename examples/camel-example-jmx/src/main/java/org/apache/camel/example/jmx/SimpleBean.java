begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|javax
operator|.
name|management
operator|.
name|AttributeChangeNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationBroadcasterSupport
import|;
end_import

begin_comment
comment|/**  * Our business logic which also is capable of broadcasting JMX notifications,  * such as an attribute being changed.  */
end_comment

begin_class
DECL|class|SimpleBean
specifier|public
class|class
name|SimpleBean
extends|extends
name|NotificationBroadcasterSupport
implements|implements
name|ISimpleMXBean
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
DECL|field|sequence
specifier|private
name|int
name|sequence
decl_stmt|;
DECL|field|tick
specifier|private
name|int
name|tick
decl_stmt|;
DECL|method|tick ()
specifier|public
name|void
name|tick
parameter_list|()
throws|throws
name|Exception
block|{
name|tick
operator|++
expr_stmt|;
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-dd-MM'T'HH:mm:ss"
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
literal|"2010-07-01T10:30:15"
argument_list|)
decl_stmt|;
name|long
name|timeStamp
init|=
name|date
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|AttributeChangeNotification
name|acn
init|=
operator|new
name|AttributeChangeNotification
argument_list|(
name|this
argument_list|,
name|sequence
operator|++
argument_list|,
name|timeStamp
argument_list|,
literal|"attribute changed"
argument_list|,
literal|"stringValue"
argument_list|,
literal|"string"
argument_list|,
name|tick
operator|-
literal|1
argument_list|,
name|tick
argument_list|)
decl_stmt|;
name|sendNotification
argument_list|(
name|acn
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

