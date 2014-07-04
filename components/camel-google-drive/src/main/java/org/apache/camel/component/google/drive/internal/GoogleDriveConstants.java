begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.google.drive.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|internal
package|;
end_package

begin_comment
comment|/**  * Constants for GoogleDrive component.  */
end_comment

begin_interface
DECL|interface|GoogleDriveConstants
specifier|public
interface|interface
name|GoogleDriveConstants
block|{
comment|// suffix for parameters when passed as exchange header properties
DECL|field|PROPERTY_PREFIX
name|String
name|PROPERTY_PREFIX
init|=
literal|"CamelGoogleDrive."
decl_stmt|;
comment|// thread profile name for this component
DECL|field|THREAD_PROFILE_NAME
name|String
name|THREAD_PROFILE_NAME
init|=
literal|"CamelGoogleDrive"
decl_stmt|;
block|}
end_interface

end_unit

