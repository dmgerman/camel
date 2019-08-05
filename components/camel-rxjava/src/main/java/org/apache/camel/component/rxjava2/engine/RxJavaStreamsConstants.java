begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rxjava2.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rxjava2
operator|.
name|engine
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
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * Useful constants used in the Camel Reactive Streams component.  */
end_comment

begin_class
DECL|class|RxJavaStreamsConstants
specifier|public
specifier|final
class|class
name|RxJavaStreamsConstants
block|{
DECL|field|SERVICE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"rxjava2"
decl_stmt|;
comment|/**      * An implementation of the {@link AsyncCallback} that does nothing.      */
DECL|field|EMPTY_ASYNC_CALLBACK
specifier|public
specifier|static
specifier|final
name|AsyncCallback
name|EMPTY_ASYNC_CALLBACK
init|=
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{         }
block|}
decl_stmt|;
DECL|method|RxJavaStreamsConstants ()
specifier|private
name|RxJavaStreamsConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

